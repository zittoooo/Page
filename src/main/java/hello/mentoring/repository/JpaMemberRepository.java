package hello.mentoring.repository;

import hello.mentoring.dao.MemberDao;
import hello.mentoring.model.Member;
import hello.mentoring.model.UploadFile;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepo {
    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        MemberDao memberDao = new MemberDao();
        memberDao.setMemberName(member.getMemberName());
        memberDao.setAddress(member.getAddress());
        memberDao.setUploadFileName(member.getAttachFile().getUploadFileName());
        memberDao.setStoreFileName(member.getAttachFile().getStoreFileName());

        em.persist(memberDao);
        em.flush();
        member.setId(memberDao.getId());
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        MemberDao memberDao = em.find(MemberDao.class, id);
        Member member = memberDaoToMember(memberDao);
        return Optional.ofNullable(member);
    }

    @Override
    public List<Member> findAll() {
        List<MemberDao> result = em.createQuery("select m from MemberDao m", MemberDao.class).getResultList();
        List<Member> memberList = new ArrayList<>();

        for (MemberDao dao : result) {
            memberList.add(memberDaoToMember(dao));
        }
        return memberList;
    }

    @Override
    public Member update(Long memberId, Member update) {
        MemberDao find = findByIdDao(memberId);
        find.setMemberName(update.getMemberName());
        find.setAddress(update.getAddress());
        find.setUploadFileName(update.getAttachFile().getUploadFileName());
        find.setStoreFileName(update.getAttachFile().getStoreFileName());
        return update;
    }

    @Override
    public void delete(Long memberId) {
        MemberDao find = findByIdDao(memberId);
        em.remove(find);
//        em.createQuery("delete from MemberDao m where m.id = :memberId", MemberDao.class);
    }

    private Member memberDaoToMember(MemberDao dao) {
        Member member = new Member();
        member.setId(dao.getId());
        member.setMemberName(dao.getMemberName());
        member.setAddress(dao.getAddress());
        member.setAttachFile(new UploadFile(dao.getUploadFileName(), dao.getStoreFileName()));
        return  member;
    }

    private MemberDao findByIdDao(Long id) {
        return em.find(MemberDao.class, id);
    }
}
