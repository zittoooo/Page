package hello.mentoring.service;

import hello.mentoring.dao.MemberDao;
import hello.mentoring.model.Member;
import hello.mentoring.model.MemberForm;
import hello.mentoring.model.UploadFile;
import hello.mentoring.repository.FileStore;
import hello.mentoring.repository.MemberFileRepository;
import hello.mentoring.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final FileStore fileStore;
    private final MemberFileRepository memberFileRepository;

    public MemberService(MemberRepository memberRepository, FileStore fileStore, MemberFileRepository fileRepository) {
        this.memberRepository = memberRepository;
        this.fileStore = fileStore;
        this.memberFileRepository = fileRepository;
    }

    /**
     * Member를 MemberForm으로 변환
     * @param member
     * @return MemberForm
     */
    public MemberForm member2Form(Member member){
        return MemberForm.builder()
                .check(false)
                .id(member.getId())
                .memberName(member.getMemberName())
                .address(member.getAddress())
                .fileName(member.getAttachFile().getUploadFileName())
                .build();
    }

    /**
     * 사용자가 입력한 form내용을 Member로 변환
     * @param form
     * @return Member
     * @throws IOException
     */
    public Member form2Member(MemberForm form, UploadFile uploadFile) {
        Member member = Member.builder()
                .check(false)
                .memberName(form.getMemberName())
                .address(form.getAddress())
                .attachFile(uploadFile).build();
        return member;
    }

    public MemberDao member2Dao(Member m) {
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
    public MemberForm findMember2Form(Long memberId) {
        Member member = findById(memberId);
        return member2Form(member);
    }

    /**
     * DB 에서 모든 member를 찾아 반환
     * @return List<Member>
     */
    public List<Member> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(m -> new Member(m.getId(), m.getMemberName(),
                        m.getAddress(), m.getUploadFileName(), m.getStoreFileName()))
                .collect(Collectors.toList());
    }

    /**
     * DB에서 memberID로 회원을 찾아 반환
     * @param memberId
     * @return Member
     */
    public Member findById(Long memberId) {
        Optional<MemberDao> memberDao = memberRepository.findById(memberId);
        MemberDao member = memberDao.get();
//        if (member == null)
//            throw new NullPointerException();
        return Member.builder()
                .id(member.getId())
                .memberName(member.getMemberName())
                .address(member.getAddress())
                .attachFile(new UploadFile(member.getUploadFileName(), member.getStoreFileName()))
                .build();
    }

    /**
     * 회원의 파일 저장, DB에 회원 저장, 파일DB에 저장
     * @param form
     * @return Member
     */
    public Long save(MemberForm form) throws IOException {
        UploadFile uploadFile = fileStore.storeFile(form.getAttachFile());
        Member member = form2Member(form, uploadFile);
        Long id = memberRepository.save(member2Dao(member));
        member.setId(id);
        memberFileRepository.saveOnFile(member2Dao(member));
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
        memberFileRepository.updateOnFile(member2Dao(updateMember));
        return updateMember;
        // 모든 전처리가 끝나고
    }


    /**
     * DB 에서 memberId로 회원찾고 회원의 파일 지우고 DB 에서 member 지운 뒤 회원 목록 리턴
     * @param memberId
     * @return List<Member>
     */
    public List<Member> deleteMember(Long memberId) {
        memberRepository.findById(memberId).ifPresent(memberDao -> {
            try {
                fileStore.deleteFile(findById(memberId));
                memberFileRepository.deleteOnFile(memberId);
                memberRepository.delete(memberId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return findAll();
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
     * 파일 저장소에서 파일 삭제
     * @param member
     */
    public void deleteFile(Member member) {
        fileStore.deleteFile(member);
    }
}
