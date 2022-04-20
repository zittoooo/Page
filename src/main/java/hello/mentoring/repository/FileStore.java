package hello.mentoring.repository;

import hello.mentoring.model.Member;
import hello.mentoring.model.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileStore {
    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        int pos = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(pos + 1);
        String storeFileName = uuid + "." + ext;
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return new UploadFile(multipartFile.getOriginalFilename(), storeFileName);
    }

    public void deleteFile(Member member) {
        File file = new File(getFullPath(member.getAttachFile().getStoreFileName()));
        System.out.println(getFullPath(member.getAttachFile().getStoreFileName()));

        if (file.exists()) {
            if (file.delete()) {
                System.out.println("MemberService.deleteMember 파일 삭제 성공");
            } else {
                System.out.println("MemberService.deleteMember 파일 삭제 실패");
            }
        } else {
            System.out.println("MemberService.deleteMember 파일이 존재하지 않습니다.");
        }
    }
}
