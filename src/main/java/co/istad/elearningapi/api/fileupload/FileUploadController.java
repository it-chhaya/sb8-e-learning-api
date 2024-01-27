package co.istad.elearningapi.api.fileupload;

import co.istad.elearningapi.base.BaseSuccess;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileUploadController {

    @Value("${file-upload.server-path}")
    private String serverPath;

    @PostMapping
    BaseSuccess<?> uploadSingle(@RequestPart MultipartFile file) {
        String newFileName = UUID.randomUUID().toString();
        // Extract ext
        int lastDotIndex = file.getOriginalFilename().lastIndexOf(".");
        String ext = file.getOriginalFilename().substring(lastDotIndex + 1);
        newFileName += ".";
        newFileName += ext;

        Path path = Paths.get(serverPath + newFileName);

        try {
            Files.copy(file.getInputStream(), path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
