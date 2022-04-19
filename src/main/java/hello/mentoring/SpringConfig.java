package hello.mentoring;

import hello.mentoring.repository.FileStore;
import hello.mentoring.repository.JpaMemberRepository;
import hello.mentoring.repository.MemberRepo;
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
        return new MemberService(memberRepository(), fileStore());
    }

    @Bean
    public MemberRepo memberRepository() {
//        return new MemberRepository();
        return new JpaMemberRepository(em);
    }

    @Bean
    public FileStore fileStore() {
        return new FileStore();
    }

}