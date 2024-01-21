package co.istad.elearningapi.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BaseSuccess<T>(Integer code,
                             String message,
                             LocalDateTime timestamp,
                             Boolean status,
                             @JsonInclude(JsonInclude.Include.NON_NULL)
                             T data) {
}
