package co.istad.elearningapi.exception;

import co.istad.elearningapi.base.BaseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class FileUploadException {

    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    BaseError<?> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException e) {
        return BaseError.builder()
                .errors(e.getMessage())
                .status(false)
                .code(e.getStatusCode().value())
                .message("File upload is failed!")
                .timestamp(LocalDateTime.now())
                .build();
    }

}
