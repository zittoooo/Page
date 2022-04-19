package hello.mentoring.repository;

import hello.mentoring.model.Member;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface MemberRepo {
    Member save(Member member);
    Optional<Member> findById(Long id);
    List<Member> findAll();
    Member update(Long memberId, Member update);
    void delete(Long memberId);
}
