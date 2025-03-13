package hello.hello_spring.service;

import hello.hello_spring.domain.Member;
import hello.hello_spring.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// 스프링 컨테이너에 등록해야 하는 객체를 참조할 수 있도록 하기 위함. -> 컴퍼넌트 스캔
// @Service

@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    // 해당 어노테이션을 통해, 스프링이 생성자에 해당 객체를 넣어줌.(스프링 컨테이너에 있는) -> DI
    // @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    
    public Long join(Member member) {
        vaildateDuplicateMember(member);

        memberRepository.save(member);
        return member.getId();
    }

    private void vaildateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    public List<Member> findMember() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
