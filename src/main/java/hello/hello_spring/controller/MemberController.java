package hello.hello_spring.controller;

import hello.hello_spring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

// Controller 어노테이션을 통해, 스프링 컨테이너가 뜰 때 해당 객체를 생성해서 관리(스프링 컨테이너가). -> 컴퍼넌트 스캔
@Controller
public class MemberController {

    private final MemberService memberService;

    // 스프링 컨테이너에 해당 MemberService 객체를 연결(단 하나만 생성해서 관리하기 위함 -> 싱글톤)
    // 생성자 주입 방식. 어차피 한 번만 세팅될거라, 굳이 세터 주입이나 필드 주입대신, 생성자 주입 방식이 적절하다.
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
}
