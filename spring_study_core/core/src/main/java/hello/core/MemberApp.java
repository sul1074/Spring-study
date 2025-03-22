package hello.core;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

public class MemberApp {
    public static void main(String[] args) {
//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService();

        // 빈을 관리하는 스프링 컨테이너
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        
        // 메서드 이름으로 등록된 빈을 찾음
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);
        Member member = new Member(1L, "MemberA", Grade.VIP);

        memberService.join(member);
        Member findMember = memberService.findMember(1L);

        System.out.println("Member = " + member.getName());
        System.out.println("findMember = " + findMember.getName());
    }
}
