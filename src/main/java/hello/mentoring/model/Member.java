package hello.mentoring.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Member {
    private boolean check;
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
}
