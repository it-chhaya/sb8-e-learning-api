package co.istad.elearningapi.api.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record RegisterDto(@NotBlank
                          String username,
                          @NotBlank
                          @Email
                          String email,
                          @NotBlank
                          String password,
                          @NotBlank
                          String passwordConfirmation,
                          @NotEmpty
                          Set<@NotNull Integer> roleIds) {
}
