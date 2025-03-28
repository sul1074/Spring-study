package hello.servlet.domain.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 동시성 고려되지 않음. 실무에서는 이렇게 X
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    private static final MemberRepository instance = new MemberRepository();

    private MemberRepository() {}

    public static MemberRepository getInstance() {
        return instance;
    }

    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);

        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }

    public List<Member> findALl() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
