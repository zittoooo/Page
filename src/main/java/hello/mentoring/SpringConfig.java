package hello.mentoring;

import hello.mentoring.repository.*;
//import hello.mentoring.repository.MemberRepository;
import hello.mentoring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class SpringConfig {

    private EntityManager em;

    @Autowired
    public SpringConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository(), fileStore(), FileRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
//        return new MemberRepository();
        return new JpaMemberRepositoryImpl(em);
    }

    @Bean
    public FileStore fileStore() {
        return new FileStore();
    }

    @Bean
    public MemberFileRepository FileRepository() { return new MemberFileRepositoryImpl(); }

}