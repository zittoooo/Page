package hello.mentoring.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class MemberForm {
    private Boolean check;

    private Long id;
    private String memberName;
    private String address;
    private MultipartFile attachFile;
    private String fileName;
}
