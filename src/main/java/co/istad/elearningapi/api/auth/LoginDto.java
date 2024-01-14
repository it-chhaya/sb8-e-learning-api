package co.istad.elearningapi.api.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(@NotBlank String username,
                       @NotBlank String password) {
}
