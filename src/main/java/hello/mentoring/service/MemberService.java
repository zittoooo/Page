package hello.mentoring.service;

import hello.mentoring.dao.MemberDao;
import hello.mentoring.model.Member;
import hello.mentoring.model.MemberForm;
import hello.mentoring.model.UploadFile;
import hello.mentoring.repository.FileStore;
import hello.mentoring.repository.MemberFileRepository;
import hello.mentoring.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;

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
     * 사용자가 입력한 form내용을 Member로 변환
     * @param form
     * @return Member
     * @throws IOException
     */
    public Member form2Member(MemberForm form) {
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
    public MemberForm member2Form(Member member){
        return MemberForm.builder()
                .check(false)
                .id(member.getId())
                .memberName(member.getMemberName())
                .address(member.getAddress())
                .fileName(member.getAttachFile().getUploadFileName())
                .build();
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

    public Member dao2Member(MemberDao dao) {
        return Member.builder()
                .id(dao.getId())
                .memberName(dao.getMemberName())
                .address(dao.getAddress())
                .attachFile(new UploadFile(dao.getUploadFileName(), dao.getStoreFileName()))
                .build();
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
        memberFileRepository.findByNameOnFile(member);
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
     * 회원의 파일 저장, DB에 회원 저장, 파일DB에 저장 IOException이 발생하면 다 지우고 사용자에게 다시 입력받음
     * @param form
     * @return Member
     */
    public Long save(MemberForm form) throws IOException {
        Member member = form2Member(form);

        UploadFile uploadFile = fileStore.storeFile(form.getAttachFile());
        member.setAttachFile(uploadFile);
        memberFileRepository.saveOnFile(member2Dao(member));

        Long id = memberRepository.save(member2Dao(member));

//        DB 저장하다가 문제생기면
//        fileStore.deleteFile(member);
//        memberFileRepository.deleteOnFile(member);
        member.setId(id);
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
            fileStore.deleteFile(member2Dao(findMember));
            UploadFile uploadFile = fileStore.storeFile(form.getAttachFile());
            findMember.setAttachFile(uploadFile);
        }
        memberFileRepository.updateOnFile(member2Dao(findMember));
        Member updateMember = memberRepository.update(memberId, findMember);
        return updateMember;
        // 모든 전처리가 끝나고
    }

    /**
     * DB 에서 memberId로 회원찾고 회원의 파일 지우고 DB 에서 member 지운 뒤 회원 목록 리턴
     * @param memberId
     * @return List<Member>
     */
    public List<Member> deleteMemberById(Long memberId) {
        memberRepository.findById(memberId).ifPresent(memberDao -> {
            try {
                fileStore.deleteFile(memberDao);
                memberFileRepository.deleteOnFile(memberDao);
                memberRepository.delete(memberDao.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return findAll();
    }

    /**
     * fileStore -> FileRepository -> DB 순서로 삭제
     * @param dao
     */
    public void deleteMemberByDao(MemberDao dao) {
        fileStore.deleteFile(dao);
        try {
            memberFileRepository.deleteOnFile(dao);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (dao.getId() != null) {
            memberRepository.delete(dao.getId());
        }
    }
}
