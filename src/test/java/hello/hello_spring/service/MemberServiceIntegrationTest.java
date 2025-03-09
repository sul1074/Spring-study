package hello.hello_spring.service;

import hello.hello_spring.repository.MemberRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import hello.hello_spring.domain.Member;
import hello.hello_spring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest // 스프링 컨테이너와 함께 테스트 실행
@Transactional // 테스트 후, db가 롤백되게 해줌
class MemberServiceIntegrationTest {

    @Autowired MemberService memberService ;
    @Autowired MemberRepository memberRepository;

    @Test
    void join() {
        // given
        Member member = new Member();
        member.setName("spring");

        // when
        Long saveId = memberService.join(member);

        // then
        Member findMember  = memberService.findOne(member.getId()).get();

        // 실제 값과 기대 값을 비교
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void duplicateMemberException() {
        // given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        // when
        memberService.join(member1);

        // 동일한 예외를 던지는지 체크
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

        // fail
        //assertThrows(NullPointerException.class, () -> memberService.join(member2));

//        try {
//            memberService.join(member2);
//        } catch (IllegalStateException e) {
//            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원 아이입니다.");
//        }


        // then

    }
}