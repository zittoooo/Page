package hello.mentoring.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
@AllArgsConstructor
public class Member {
    private Boolean check;
    private Long id;
    private String memberName;
    private String address;
    private UploadFile attachFile;

    public Member() {}

    public Member(String memberName, String address) {
        this.check = false;
        this.memberName = memberName;
        this.address = address;
    }

    public Member(Long id, String name, String address, String upload, String store) {
        this.check = false;
        this.id = id;
        this.memberName = name;
        this.address = address;
        this.attachFile = new UploadFile(upload, store);
    }
}
