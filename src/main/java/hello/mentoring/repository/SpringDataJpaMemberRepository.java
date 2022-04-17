//package hello.mentoring.repository;
//
//import hello.mentoring.model.Member;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepo {
//
//
//    @Override
//    Member save(Member member);
//
//    @Override
//    Member findById(Long id);
//
//    @Override
//    Optional<Member> findById(Long aLong);
//
//    @Override
//    List<Member> findAll();
//
//    @Override
//    Member update(Long memberId, Member update);
//    @Override
//    void delete(Long memberId);
//}
