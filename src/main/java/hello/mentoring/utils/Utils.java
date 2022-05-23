package hello.mentoring.utils;

import hello.mentoring.dao.MemberDao;
import hello.mentoring.model.Member;
import hello.mentoring.model.MemberForm;
import hello.mentoring.model.UploadFile;

import java.io.IOException;

public class Utils {
    /**
     * 사용자가 입력한 form내용을 Member로 변환
     * @param form
     * @return Member
     * @throws IOException
     */
    public static Member form2Member(MemberForm form) {
        Member member = Member.builder()
                .check(false)
                .memberName(form.getMemberName())
                .address(form.getAddress())
                .build();
        return member;
    }

    /**
     * Member를 MemberForm으로 변환
     * @param member
     * @return MemberForm
     */
    public static MemberForm member2Form(Member member){
        return MemberForm.builder()
                .check(false)
                .id(member.getId())
                .memberName(member.getMemberName())
                .address(member.getAddress())
                .fileName(member.getAttachFile().getUploadFileName())
                .build();
    }

    public static MemberDao member2Dao(Member m) {
        MemberDao mDao = new MemberDao();
        if (m.getId() != null) {
            mDao.setId(m.getId());
        }
        mDao.setMemberName(m.getMemberName());
        mDao.setAddress(m.getAddress());
        if (m.getAttachFile() != null) {
            mDao.setUploadFileName(m.getAttachFile().getUploadFileName());
            mDao.setStoreFileName(m.getAttachFile().getStoreFileName());
        }
        return mDao;
    }

    public static Member dao2Member(MemberDao dao) {
        return Member.builder()
                .id(dao.getId())
                .memberName(dao.getMemberName())
                .address(dao.getAddress())
                .attachFile(new UploadFile(dao.getUploadFileName(), dao.getStoreFileName()))
                .build();
    }


}
