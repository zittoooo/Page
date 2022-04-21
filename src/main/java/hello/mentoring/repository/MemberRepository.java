package hello.mentoring.repository;

import hello.mentoring.model.Member;
import org.springframework.stereotype.Repository;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MemberRepository {
    private final Map<Long, Member> store = new HashMap<>();
    private static Long sequence = 0L;

    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public Member update(Long memberId, Member update) {
        Member findMember = findById(memberId);
        findMember.setMemberName(update.getMemberName());
        findMember.setAddress(update.getAddress());
        findMember.setAttachFile(update.getAttachFile());
        return findMember;
    }

    public void delete(Long memberId) {
        store.remove(memberId);
    }
}
