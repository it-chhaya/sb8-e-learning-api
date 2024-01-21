package co.istad.elearningapi.api.auth;

import co.istad.elearningapi.api.user.UserMapper;
import co.istad.elearningapi.api.user.UserRepository;
import co.istad.elearningapi.api.user.UserService;
import co.istad.elearningapi.api.user.web.UserCreationDto;
import co.istad.elearningapi.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JavaMailSender javaMailSender;

    private final DaoAuthenticationProvider daoAuthProvider;
    private final JwtAuthenticationProvider jwtAuthProvider;
    private final JwtEncoder jwtAccessTokenEncoder;
    private JwtEncoder jwtRefreshTokenEncoder;

    @Autowired
    @Qualifier("jwtRefreshTokenEncoder")
    public void setJwtRefreshTokenEncoder(JwtEncoder jwtRefreshTokenEncoder) {
        this.jwtRefreshTokenEncoder = jwtRefreshTokenEncoder;
    }

    @Override
    public AuthDto refreshToken(RefreshTokenDto refreshTokenDto) {
        Authentication auth = new BearerTokenAuthenticationToken(refreshTokenDto.refreshToken());
        auth = jwtAuthProvider.authenticate(auth);
        return this.createToken(auth);
    }

    @Override
    public AuthDto login(LoginDto loginDto) {
        Authentication auth = new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password());
        auth = daoAuthProvider.authenticate(auth);
        return this.createToken(auth);
    }

    private AuthDto createToken(Authentication auth) {

        String refreshToken;

        if (auth.getCredentials() instanceof Jwt jwt) {
            Instant now = Instant.now();
            Instant expiresAt = jwt.getExpiresAt();
            Duration duration = Duration.between(now, expiresAt);
            long daysUntilExpired = duration.toDays();
            if (daysUntilExpired <= 7) {
                refreshToken = createRefreshToken(auth);
            } else {
                refreshToken = jwt.getTokenValue();
            }
        } else {
            refreshToken = createRefreshToken(auth);
        }

        return AuthDto.builder()
                .tokenType("Bearer")
                .accessToken(this.createAccessToken(auth))
                .refreshToken(refreshToken)
                .build();
    }

    private String createAccessToken(Authentication auth) {

        Instant now = Instant.now();

        String authorities = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .issuer(auth.getName())
                .issuedAt(now)
                .claim("authorities", authorities)
                .subject("Access Token")
                .audience(List.of("iOS", "Android"))
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .build();

        return jwtAccessTokenEncoder.encode(
                JwtEncoderParameters.from(jwtClaimsSet)
        ).getTokenValue();
    }

    private String createRefreshToken(Authentication auth) {

        Instant now = Instant.now();

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .issuer(auth.getName())
                .issuedAt(now)
                //.claim("authorities", scope)
                .expiresAt(now.plus(30, ChronoUnit.DAYS))
                .subject("Refresh Token")
                .audience(List.of("iOS", "Android"))
                .build();

        return jwtRefreshTokenEncoder.encode(
                JwtEncoderParameters.from(jwtClaimsSet)
        ).getTokenValue();
    }


    @Override
    public void register(RegisterDto registerDto) {

        if (!registerDto.password().equals(registerDto.passwordConfirmation())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Password is not match!");
        }

        UserCreationDto userCreationDto = userMapper.mapRegisterDtoToUserCreationDto(registerDto);
        userService.createNew(userCreationDto);
    }

}
