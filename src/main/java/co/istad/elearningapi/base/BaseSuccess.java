package co.istad.elearningapi.base;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BaseSuccess<T>(Integer code,
                          String message,
                          LocalDateTime timestamp,
                          Boolean status,
                          T data) {
}
