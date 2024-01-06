package co.istad.elearningapi.base;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BaseError<T>(Integer code,
                           String message,
                           LocalDateTime timestamp,
                           Boolean status,
                           T errors) {
}
