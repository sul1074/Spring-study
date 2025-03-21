package hello.core.member;

public class MemberServiceImpl implements MemberService {

    // MemberServiceImpl이 추상화와 구체화 둘 다에 의존 -> DIP 위반
    private final MemberRepository memberRepository = new MemoryMemberRepository();

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
