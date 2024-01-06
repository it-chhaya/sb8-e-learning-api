package co.istad.elearningapi.api.category.web;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreationDto(
        @NotBlank
        String name,
        String description,
        String icon
) {
}
