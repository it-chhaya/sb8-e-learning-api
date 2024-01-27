package co.istad.elearningapi.api.fileupload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Value("${file-upload.server-path}")
    private String serverPath;

    @Override
    public void uploadSingle(MultipartFile file) {

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

    }
}
