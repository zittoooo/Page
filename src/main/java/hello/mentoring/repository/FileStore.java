package hello.mentoring.repository;

import hello.mentoring.dao.MemberDao;
import hello.mentoring.model.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileStore {
    @Value("${file.dir}")
    private String fileDir;


    /**
     * 실제 파일이 저장된 절대 경로 반환
     * @param filename
     * @return String
     */
    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    /**
     * 유저의 upload 파일 이름을 랜덤한 이름을 하나 생성해서 파일 저장소에 저장
     * @param multipartFile
     * @return UploadFile
     * @throws IOException
     */
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

    /**
     * member의 파일 삭제
     * @param dao
     */
    public void deleteFile(MemberDao dao) {
//        File file = new File(getFullPath(member.getAttachFile().getStoreFileName()));
//        System.out.println(getFullPath(member.getAttachFile().getStoreFileName()));
        File file = new File(getFullPath(dao.getStoreFileName()));
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
