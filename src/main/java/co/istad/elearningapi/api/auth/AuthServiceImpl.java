package co.istad.elearningapi.api.auth;

import co.istad.elearningapi.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
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
    private final JwtEncoder jwtAccessTokenEncoder;

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
                .expiresAt(now.plus(3, ChronoUnit.DAYS))
                .build();

        String accessToken = jwtAccessTokenEncoder
                .encode(JwtEncoderParameters.from(jwtClaimsSet))
                .getTokenValue();

        return AuthDto.builder()
                .tokenType("Bearer")
                .accessToken(accessToken)
                .build();
    }

}
