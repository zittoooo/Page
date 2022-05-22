package hello.mentoring.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
@Data
@Builder
@AllArgsConstructor
public class MemberForm {
    private Boolean check;

    private Long id;

    @NotBlank
    private String memberName;
    private String address;
    private MultipartFile attachFile;
    private String fileName;

    public MemberForm() {}
}
