package co.istad.elearningapi.api.fileupload;

import lombok.Builder;

@Builder
public record FileUploadDto(String name,
                            String uri,
                            String extension,
                            Long size,
                            String fileType) {
}
