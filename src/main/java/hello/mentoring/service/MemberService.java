package hello.mentoring.service;

import hello.mentoring.model.Member;
import hello.mentoring.model.MemberForm;
import hello.mentoring.model.UploadFile;
import hello.mentoring.repository.FileStore;
import hello.mentoring.repository.MemberRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    public Member makeMember(MemberForm form) throws IOException {
        UploadFile uploadFile = fileStore.storeFile(form.getAttachFile());
        Member member = new Member();
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

    public MemberForm findMemberConvertForm(Long memberId) {
        Member member = findById(memberId);
        return makeForm(member);
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


    public List<Member> deleteMember(Long memberId) {
        memberRepository.findById(memberId).ifPresent(member -> {
            fileStore.deleteFile(member);
            memberRepository.delete(member.getId());
        });
        return memberRepository.findAll();
    }

    public UploadFile storeFile(MultipartFile attachFile) throws IOException {
        return fileStore.storeFile(attachFile);
    }

    public void deleteFile(Member member) {
        fileStore.deleteFile(member);
    }
}
