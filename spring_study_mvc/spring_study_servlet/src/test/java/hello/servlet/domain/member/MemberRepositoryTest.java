package hello.servlet.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryTest {

    MemberRepository memberRepository = MemberRepository.getInstance();

    @AfterEach
    void afterEach() {
        memberRepository.clearStore();
    }


    @Test
    void save() {
        // given
        Member member = new Member("hello", 20);

        // when
        memberRepository.save(member);

        // then
        Member findMember = memberRepository.findById(member.getId());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void findALl() {
        // given
        Member member1 = new Member("member1", 20);
        Member member2 = new Member("member1", 30);
        Member member3 = new Member("member1", 40);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        // when
        List<Member> memberList = memberRepository.findALl();

        // then
        assertThat(memberList.size()).isEqualTo(3);
        assertThat(memberList).contains(member1, member2, member3);
    }

}