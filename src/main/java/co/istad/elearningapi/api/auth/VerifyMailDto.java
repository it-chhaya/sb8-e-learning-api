package co.istad.elearningapi.api.auth;

import jakarta.validation.constraints.NotBlank;

public record VerifyMailDto(@NotBlank
                            String email) {
}
