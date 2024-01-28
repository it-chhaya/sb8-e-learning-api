package co.istad.elearningapi.api.fileupload;

import co.istad.elearningapi.base.BaseSuccess;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @PostMapping
    BaseSuccess<?> uploadSingle(@RequestPart MultipartFile file) {
        return BaseSuccess.builder()
                .code(HttpStatus.OK.value())
                .status(true)
                .message("File has been uploaded successfully")
                .data(fileUploadService.uploadSingle(file))
                .timestamp(LocalDateTime.now())
                .build();
    }

}
