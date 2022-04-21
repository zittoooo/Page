package hello.mentoring.service;

import hello.mentoring.dto.FileDto;
import hello.mentoring.model.Member;
import hello.mentoring.model.MemberForm;
import hello.mentoring.model.UploadFile;
import hello.mentoring.repository.FileStore;
import hello.mentoring.repository.MemberRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Optional;

@Transactional
public class MemberService {
    private final MemberRepo memberRepository;
    private final FileStore fileStore;

    public MemberService(MemberRepo memberRepository, FileStore fileStore) {
        this.memberRepository = memberRepository;
        this.fileStore = fileStore;
    }

    @Value("${file.dir}")
    private String fileDir;


    /**
     * 사용자가 입력한 form내용을 Member로 변환
     * @param form
     * @return Member
     * @throws IOException
     */
    public Member makeMember(MemberForm form) throws IOException {
        UploadFile uploadFile = fileStore.storeFile(form.getAttachFile());
        Member member = new Member();
        member.setMemberName(form.getMemberName());
        member.setAddress(form.getAddress());
        member.setAttachFile(uploadFile);
        return member;
    }

    /**
     * Member를 MemberForm으로 변환
     * @param member
     * @return MemberForm
     */
    public MemberForm makeForm(Member member){
        MemberForm form = new MemberForm();
        form.setId(member.getId());
        form.setMemberName(member.getMemberName());
        form.setAddress(member.getAddress());
        return form;
    }

    /**
     * DB에서 memberId로 member를 찾아 form으로 변환
     * @param memberId
     * @return MemberForm
     */
    public MemberForm findMemberConvertForm(Long memberId) {
        Member member = findById(memberId);
        return makeForm(member);
    }

    /**
     * DB에서 모든 member를 찾아 반환
     * @return List<Member>
     */
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    /**
     * DB에서 memberID로 member찾아 반환
     * @param memberId
     * @return Member
     */
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).get();
    }

    /**
     * DB에 인자로 들어온 member 저장하고 반환
     * @param member
     * @return Member
     */
    public Member save(Member member) {
       return memberRepository.save(member);
    }

    /**
     * DB에서 memberId로 member를 찾아서 수정 내용이 담긴 member로 수정
     * @param memberId
     * @param member
     * @return 수정된 Member
     */
    public Member updateRepository(Long memberId, Member member) {
        return memberRepository.update(memberId, member);
    }

    /**
     * memberId로 member를 찾아서 유저가 입력한 form 내용으로 수정
     * @param memberId
     * @param form
     * @return Member
     * @throws IOException
     */
    public Member updateMember(Long memberId, MemberForm form) throws IOException {
        Member findMember = findById(memberId);

        if (!form.getMemberName().isEmpty()) {
            findMember.setMemberName(form.getMemberName());
        }
        if (!form.getAddress().isEmpty()) {
            findMember.setAddress(form.getAddress());
        }
        if (!form.getAttachFile().isEmpty()) {
            fileStore.deleteFile(findMember);
            UploadFile uploadFile = fileStore.storeFile(form.getAttachFile());
            findMember.setAttachFile(uploadFile);
        }
        return findMember;
        // 모든 전처리가 끝나고
    }


    /**
     * DB에서 memberId로 member찾고 member의 파일 지우고 DB에서 member 지운 뒤 회원 목록 리턴
     * @param memberId
     * @return List<Member>
     */
    public List<Member> deleteMember(Long memberId) {
        memberRepository.findById(memberId).ifPresent(member -> {
            fileStore.deleteFile(member);
            memberRepository.delete(member.getId());
        });
        return memberRepository.findAll();
    }


    /**
     * 유저가 업로드한 파일을 파일 저장소에 저장하고 UploadFile 반환
     * @param attachFile
     * @return UploadFile
     * @throws IOException
     */
    public UploadFile storeFile(MultipartFile attachFile) throws IOException {
        return fileStore.storeFile(attachFile);
    }

    /**
     * 파일 저장소에서 member의 파일 삭제
     * @param member
     */
    public void deleteFile(Member member) {
        fileStore.deleteFile(member);
    }

}
