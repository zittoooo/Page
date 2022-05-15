package hello.mentoring.repository;

import hello.mentoring.dao.MemberDao;
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
    public MemberDao findById(Long id) {
        Optional<MemberDao> dao = Optional.ofNullable(em.find(MemberDao.class, id));

        if (dao.isPresent()) {
            return dao.get();
        } else {
            return null;
        }
    }

    @Override
    public List<MemberDao> findAll() {
        List<MemberDao> result = em.createQuery("select m from MemberDao m", MemberDao.class).getResultList();
        return result;
    }

    @Override
    public MemberDao update(MemberDao find, MemberDao update) {
        delete(find.getId());
        save(update);
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
