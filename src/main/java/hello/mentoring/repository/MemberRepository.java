package hello.mentoring.repository;

import hello.mentoring.dao.MemberDao;
import hello.mentoring.model.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Long save(MemberDao Member);
    Optional<MemberDao> findById(Long id);
    List<MemberDao> findAll();
    Member update(Long memberId, Member update);
    void delete(Long memberId);
}
