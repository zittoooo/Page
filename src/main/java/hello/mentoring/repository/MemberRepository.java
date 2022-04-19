//package hello.mentoring.repository;
//
//import hello.mentoring.model.Member;
//import org.springframework.stereotype.Repository;
//
//import javax.swing.text.html.Option;
//import java.util.*;
//
//public class MemberRepository implements MemberRepo{
//    private static Map<Long, Member> store = new HashMap<>();
//    private static Long sequence = 0L;
//
//    @Override
//    public Member save(Member member) {
//        member.setId(++sequence);
//        store.put(member.getId(), member);
//        return member;
//    }
//
//    @Override
//    public Optional<Member> findById(Long id) {
//        return Optional.ofNullable(store.get(id));
//    }
//
//    @Override
//    public List<Member> findAll() {
//        return new ArrayList<>(store.values());
//    }
//
//    @Override
//    public Member update(Long memberId, Member update) {
//        Member findMember = findById(memberId).get();
//        findMember.setMemberName(update.getMemberName());
//        findMember.setAddress(update.getAddress());
//        findMember.setAttachFile(update.getAttachFile());
//        return findMember;
//    }
//
//    @Override
//    public void delete(Long memberId) {
//        store.remove(memberId);
//    }
//}
