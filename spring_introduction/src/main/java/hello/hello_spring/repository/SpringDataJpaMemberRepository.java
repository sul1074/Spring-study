package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 스프링 데이터 Jpa가 자동으로 구현체를 만들어서 등록해줌
// 즉, 인터페이스 이름 만으로도 쿼리 작성, db 접근 등의 기능을 모두 알아서 작성해줌
public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {

    // select m from Member m where m.name = ? 이렇게 쿼리를 짜줌
    @Override
    Optional<Member> findByName(String name);
}
