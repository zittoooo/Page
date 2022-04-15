package hello.mentoring.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MemberForm {
    private boolean check;
    private Long id;
    private String memberName;
    private String address;
    private MultipartFile attachFile;
}
