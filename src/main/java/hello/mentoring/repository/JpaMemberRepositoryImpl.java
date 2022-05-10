package hello.mentoring.repository;

import hello.mentoring.dao.MemberDao;
import hello.mentoring.model.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaMemberRepositoryImpl implements MemberRepository {
    private final EntityManager em;

    public JpaMemberRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Long save(MemberDao memberDao) {
        em.persist(memberDao);
        em.flush();
        return memberDao.getId();
    }

    @Override
    public Optional<MemberDao> findById(Long id) {
        return Optional.ofNullable(em.find(MemberDao.class, id));
    }

    @Override
    public List<MemberDao> findAll() {
        List<MemberDao> result = em.createQuery("select m from MemberDao m", MemberDao.class).getResultList();
        return result;
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

    private MemberDao findByIdDao(Long id) {
        return em.find(MemberDao.class, id);
    }
}
