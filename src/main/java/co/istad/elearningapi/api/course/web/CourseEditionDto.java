package co.istad.elearningapi.api.course.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CourseEditionDto(
        @NotBlank
        String title,
        @NotBlank
        String description,
        @NotNull
        BigDecimal price,
        @NotNull
        Boolean isFree
) {
}
