package co.istad.elearningapi.api.fileupload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${file-upload.server-path}")
    private String serverPath;

    @Value("${file-upload.base-uri}")
    private String baseUri;

    @Override
    public FileUploadDto uploadSingle(MultipartFile file) {
        // Generate new file name with unique name
        String newFileName = UUID.randomUUID().toString();
        // Extract extension
        int lastDotIndex = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf(".");
        String ext = file.getOriginalFilename().substring(lastDotIndex + 1); // png
        newFileName = String.format("%s.%s", newFileName, ext);

        // Create absolute path of file upload
        Path path = Paths.get(serverPath + newFileName);
        try {
            Files.copy(file.getInputStream(), path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return FileUploadDto.builder()
                .name(newFileName)
                .uri(baseUri + newFileName)
                .extension(ext)
                .size(file.getSize())
                .fileType(file.getContentType())
                .build();
    }

}
