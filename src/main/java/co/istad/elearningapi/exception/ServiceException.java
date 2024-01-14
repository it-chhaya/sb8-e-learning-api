package co.istad.elearningapi.exception;

import co.istad.elearningapi.base.BaseError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ServiceException {
    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<BaseError<?>> handleServiceException(ResponseStatusException e) {
        BaseError<?> baseError = BaseError.builder()
                .errors(e.getReason())
                .status(false)
                .code(e.getStatusCode().value())
                .message("Something went wrong!")
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(baseError, e.getStatusCode());
    }
}
