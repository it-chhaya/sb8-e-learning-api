package co.istad.elearningapi.security;

import co.istad.elearningapi.util.KeyUtil;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;

import java.util.List;
import java.util.UUID;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final KeyUtil keyUtil;

    // Create in-memory users:
    // admin:admin123:ADMIN
    // subscriber:dev123:SUBSCRIBER
    /*@Bean
    InMemoryUserDetailsManager configureInMemoryUsers() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN")
                .build());
        manager.createUser(User.withUsername("subscriber")
                .password(passwordEncoder.encode("dev123"))
                .roles("SUBSCRIBER")
                .build());
        return manager;
    }*/

    @Bean
    DaoAuthenticationProvider configureAuthProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    SecurityFilterChain configureFilterChain(
            HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/users/**").hasAuthority("SCOPE_ADMIN")
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated());

        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        // Security mechanism
        // httpSecurity.httpBasic(Customizer.withDefaults());

        // JWT
        httpSecurity.oauth2ResourceServer(res -> res
                .jwt(Customizer.withDefaults()));

        httpSecurity.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }

    @Bean
    JwtDecoder jwtAccessTokenDecoder() {
        return NimbusJwtDecoder
                .withPublicKey(keyUtil
                        .getAccessTokenPublicKey()).build();
    }

    @Bean
    JwtEncoder jwtAccessTokenEncoder() {

        RSAKey rsaKey = new RSAKey.Builder(keyUtil.getAccessTokenPublicKey())
                .privateKey(keyUtil.getAccessTokenPrivateKey())
                .keyID(UUID.randomUUID().toString())
                .build();

        JWKSet jwkSet = new JWKSet(rsaKey);
        JWKSource<SecurityContext> jwkSource = (jwkSelector, context)
                -> jwkSelector.select(jwkSet);

        return new NimbusJwtEncoder(jwkSource);
    }

}
