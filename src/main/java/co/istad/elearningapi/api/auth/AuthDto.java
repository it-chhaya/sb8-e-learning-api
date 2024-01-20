package co.istad.elearningapi.api.auth;

import lombok.Builder;

@Builder
public record AuthDto(
        String tokenType,
        String accessToken,
        String refreshToken) {
}
