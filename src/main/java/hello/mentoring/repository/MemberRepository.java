package hello.mentoring.repository;

import hello.mentoring.model.Member;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.*;

@Repository
public class MemberRepository implements MemberRepo{
    private static Map<Long, Member> store = new HashMap<>();
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
