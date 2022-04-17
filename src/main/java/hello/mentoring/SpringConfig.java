//package hello.mentoring;
//
//import hello.mentoring.repository.FileStore;
//import hello.mentoring.repository.MemberRepo;
//import hello.mentoring.repository.MemberRepository;
////import hello.mentoring.repository.SpringDataJpaMemberRepository;
//import hello.mentoring.service.MemberService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class SpringConfig {
//
//    private final MemberRepo memberRepository;
//
//    @Autowired
//    public SpringConfig(MemberRepo memberRepository) {
//        this.memberRepository = memberRepository;
//    }
//
//    @Bean
//    public MemberService memberService() {
//        return new MemberService(memberRepository, fileStore());
//    }
//
//    @Bean
//    public FileStore fileStore() {
//        return new FileStore();
//    }
//}
