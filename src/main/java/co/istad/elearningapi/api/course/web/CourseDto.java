package co.istad.elearningapi.api.course.web;

import co.istad.elearningapi.api.category.web.CategoryDto;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record CourseDto(Long id,
                        String title,
                        String description,
                        String thumbnail,
                        BigDecimal price,
                        Boolean isFree,
                        LocalDateTime createdAt,
                        CategoryDto category) {
}
