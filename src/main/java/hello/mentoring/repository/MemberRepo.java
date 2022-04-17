package hello.mentoring.repository;

import hello.mentoring.model.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepo {
    Member save(Member member);
    Member findById(Long id);
    List<Member> findAll();
    Member update(Long memberId, Member update);
    void delete(Long memberId);
}
