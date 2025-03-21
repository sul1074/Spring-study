package hello.hello_spring.controller;

import hello.hello_spring.domain.Member;
import hello.hello_spring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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

    @GetMapping("/members/new")
    public String createNew() {
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());

        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMember();

        // 뷰 템플릿에 members 데이터 전송할 수 있도록
        model.addAttribute("members", members);

        return "members/memberList";

    }
}
