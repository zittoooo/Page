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
import java.util.stream.Collectors;
import static hello.mentoring.utils.Utils.*;

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
     * @return List<Member> - DB의 모든 회원
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
     * @param memberId - 조회할 id
     * @return Member - 조회된 회원
     */
    public Member findById(Long memberId) {
        MemberDao member = memberRepository.findById(memberId);
        return dao2Member(member);
    }

    /**
     * 회원의 파일 저장, DB에 회원 저장, 파일DB에 저장 IOException이 발생하면 다 지우고 사용자에게 다시 입력받음
     * @param form
     * @return Member
     */
    public Long save(MemberForm form) throws IOException {

        Member member = form2Member(form);
        if (!form.getAttachFile().isEmpty()) {
            UploadFile uploadFile = fileStore.storeFile(form.getAttachFile());
            member.setAttachFile(uploadFile);
        }
        memberFileRepository.saveOnFile(member2Dao(member));
        Long id = memberRepository.save(member2Dao(member));
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
        Member updateMember = form2Member(form);

        if (form.getAttachFile().isEmpty()) {
            updateMember.setAttachFile(findMember.getAttachFile());
        } else {
            fileStore.deleteFile(member2Dao(findMember));
            updateMember.setAttachFile(fileStore.storeFile(form.getAttachFile()));
        }
        memberFileRepository.updateOnFile(member2Dao(findMember), member2Dao(updateMember));
        MemberDao updated = memberRepository.update(member2Dao(findMember), member2Dao(updateMember));
        return dao2Member(updated);
    }

    /**
     * DB 에서 memberId로 회원찾고 회원의 파일 지우고 DB 에서 member 지운 뒤 회원 목록 리턴
     * @param memberId
     * @return List<Member>
     */
    public List<Member> deleteMemberById(Long memberId){
        MemberDao memberDao = memberRepository.findById(memberId);
        try {
            fileStore.deleteFile(memberDao);
            memberFileRepository.deleteOnFileByName(memberDao);
            memberRepository.delete(memberDao.getId());
        } catch (IOException e) {
            e.printStackTrace();
            deleteMemberByDao(memberDao);  //? ㅋㅋㅋ
        }
        return findAll();
    }

    /**
     * fileStore -> FileRepository -> DB 순서로 삭제
     * @param dao
     */
    public void deleteMemberByDao(MemberDao dao) {
        try {
            fileStore.deleteFile(dao);
            memberFileRepository.deleteOnFile(dao);
            if (dao.getId() != null) {
                memberRepository.delete(dao.getId());
            }
        } catch (IOException e) {
            e.printStackTrace();
            deleteMemberByDao(dao);
        }
    }
}
