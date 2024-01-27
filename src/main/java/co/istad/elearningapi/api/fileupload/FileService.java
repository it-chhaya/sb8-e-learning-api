package co.istad.elearningapi.api.fileupload;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    void uploadSingle(MultipartFile file);

}
