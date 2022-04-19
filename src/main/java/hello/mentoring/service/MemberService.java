package hello.mentoring.service;

import hello.mentoring.model.Member;
import hello.mentoring.model.MemberForm;
import hello.mentoring.model.UploadFile;
import hello.mentoring.repository.FileStore;
import hello.mentoring.repository.MemberRepo;
//import hello.mentoring.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Transactional
public class MemberService {
    private final MemberRepo memberRepository;
    private final FileStore fileStore;

//    @Autowired
    public MemberService(MemberRepo memberRepository, FileStore fileStore) {
        this.memberRepository = memberRepository;
        this.fileStore = fileStore;
    }

    @Value("${file.dir}")
    private String fileDir;


    public Member makeMember(MemberForm form, UploadFile uploadFile) {
        Member member = new Member();
        member.setCheck(form.isCheck());
        member.setMemberName(form.getMemberName());
        member.setAddress(form.getAddress());
        member.setAttachFile(uploadFile);
        return member;
    }

    public MemberForm makeForm(Member member){
        MemberForm form = new MemberForm();
        form.setId(member.getId());
        form.setMemberName(member.getMemberName());
        form.setAddress(member.getAddress());
        return form;
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).get();
    }

    public Member save(Member member) {
       return memberRepository.save(member);
    }

    public Member updateRepository(Long memberId, Member member) {
        return memberRepository.update(memberId, member);
    }

    public Member updateMember(MemberForm form, UploadFile uploadFile) {
        Member member = new Member();
        member.setMemberName(form.getMemberName());
        member.setAddress(form.getAddress());
        member.setAttachFile(uploadFile);
        return member;
    }

    public void deleteMember(Long memberId) {
        memberRepository.delete(memberId);
    }

    public UploadFile storeFile(MultipartFile attachFile) throws IOException {
        return fileStore.storeFile(attachFile);
    }

}
