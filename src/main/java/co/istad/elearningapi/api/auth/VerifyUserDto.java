package co.istad.elearningapi.api.auth;

import jakarta.validation.constraints.NotBlank;

public record VerifyUserDto(@NotBlank String email,
                            @NotBlank String verifiedCode) {
}
