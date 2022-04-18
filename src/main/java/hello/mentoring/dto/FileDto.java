package hello.mentoring.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.UrlResource;

@Getter @Setter
public class FileDto {
    UrlResource resource;
    String contentDisposition;
}
