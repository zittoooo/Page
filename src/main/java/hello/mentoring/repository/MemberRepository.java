package hello.mentoring.repository;

import hello.mentoring.dao.MemberDao;
import hello.mentoring.model.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Long save(MemberDao Member);
    MemberDao findById(Long id);
    List<MemberDao> findAll();
    MemberDao update(MemberDao find, MemberDao update);
    void delete(Long memberId);
}
