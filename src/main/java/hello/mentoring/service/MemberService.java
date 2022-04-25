package hello.mentoring.service;

import hello.mentoring.dao.MemberDao;
import hello.mentoring.model.Member;
import hello.mentoring.model.MemberForm;
import hello.mentoring.model.UploadFile;
import hello.mentoring.repository.FileStore;
import hello.mentoring.repository.MemberFileRepository;
import hello.mentoring.repository.MemberRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Transactional
public class MemberService {
    private final MemberRepo memberRepository;
    private final FileStore fileStore;
    private final MemberFileRepository memberFileRepository;

    public MemberService(MemberRepo memberRepository, FileStore fileStore, MemberFileRepository fileRepository) {
        this.memberRepository = memberRepository;
        this.fileStore = fileStore;
        this.memberFileRepository = fileRepository;
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

    public MemberDao makeMemberDao(Member m) {
        MemberDao mDao = new MemberDao();
        if (m.getId() != null) {
            mDao.setId(m.getId());
        }
        mDao.setMemberName(m.getMemberName());
        mDao.setAddress(m.getAddress());
        mDao.setUploadFileName(m.getAttachFile().getUploadFileName());
        mDao.setStoreFileName(m.getAttachFile().getStoreFileName());
        return mDao;
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
     * @param form
     * @return Member
     */
    public Long save(MemberForm form) throws IOException {

        Member member = makeMember(form);
        Long id = memberRepository.save(makeMemberDao(member));
        member.setId(id);
        memberFileRepository.saveOnFile(makeMemberDao(member));
        return id;
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
        Member updateMember = memberRepository.update(memberId, findMember);
        memberFileRepository.updateOnFile(makeMemberDao(findMember));
        return updateMember;
        // 모든 전처리가 끝나고
    }


    /**
     * DB에서 memberId로 member찾고 member의 파일 지우고 DB에서 member 지운 뒤 회원 목록 리턴
     * @param memberId
     * @return List<Member>
     */
    public List<Member> deleteMember(Long memberId) {
        memberRepository.findById(memberId).ifPresent(member -> {
            memberRepository.delete(member.getId());
            fileStore.deleteFile(member);
            try {
                memberFileRepository.deleteOnFile(memberId);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
