package co.istad.elearningapi.api.auth;

import co.istad.elearningapi.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

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

        Jwt jwt = (Jwt) auth.getPrincipal();

        log.info(auth.getName());
        log.info(jwt.getClaimAsString("scope"));
        log.info(jwt.getSubject());

        Instant now = Instant.now();

        JwtClaimsSet jwtAccessTokenClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .issuer(auth.getName())
                .issuedAt(now)
                .claim("scope", jwt.getClaimAsString("scope"))
                .subject("Access Token")
                .audience(jwt.getAudience())
                .expiresAt(now.plus(1, ChronoUnit.MINUTES))
                .build();

        String accessToken = jwtAccessTokenEncoder
                .encode(JwtEncoderParameters.from(jwtAccessTokenClaimsSet))
                .getTokenValue();

        return AuthDto.builder()
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshTokenDto.refreshToken())
                .build();
    }

    @Override
    public AuthDto login(LoginDto loginDto) {

        Authentication auth = new UsernamePasswordAuthenticationToken(
                loginDto.username(),
                loginDto.password()
        );

        // Invoke security process
        auth = daoAuthProvider.authenticate(auth);

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        log.info(userDetails.getUser().getEmail());

        String scope = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(" "));

        Instant now = Instant.now();

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .issuer(userDetails.getUser().getEmail())
                .issuedAt(now)
                .claim("scope", scope)
                .subject("Access Token")
                .audience(List.of("iOS", "Android"))
                .expiresAt(now.plus(1, ChronoUnit.MINUTES))
                .build();

        JwtClaimsSet jwtRefreshTokenClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .issuer(userDetails.getUser().getEmail())
                .issuedAt(now)
                .claim("scope", scope)
                .subject("Refresh Token")
                .audience(List.of("iOS", "Android"))
                .expiresAt(now.plus(7, ChronoUnit.DAYS))
                .build();

        String accessToken = jwtAccessTokenEncoder
                .encode(JwtEncoderParameters.from(jwtClaimsSet))
                .getTokenValue();

        String refreshToken = jwtRefreshTokenEncoder
                .encode(JwtEncoderParameters.from(jwtRefreshTokenClaimsSet))
                .getTokenValue();

        return AuthDto.builder()
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
