package co.istad.elearningapi.api.fileupload;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

    FileUploadDto uploadSingle(MultipartFile file);

}
