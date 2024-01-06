package co.istad.elearningapi.api.course.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CourseCreationDto(@NotBlank
                                String title,
                                @NotBlank
                                String description,
                                @NotBlank
                                String thumbnail,
                                @NotNull
                                BigDecimal price,
                                @NotNull
                                Boolean isFree,
                                @NotNull
                                @Positive
                                Integer categoryId) {
}
