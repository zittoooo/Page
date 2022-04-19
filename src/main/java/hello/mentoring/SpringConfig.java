package hello.mentoring;

import hello.mentoring.repository.FileStore;
import hello.mentoring.repository.MemberRepo;
import hello.mentoring.repository.MemberRepository;
import hello.mentoring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository(), fileStore());
    }

    @Bean
    public MemberRepo memberRepository() {
        return new MemberRepository();
    }

    @Bean
    public FileStore fileStore() {
        return new FileStore();
    }

}