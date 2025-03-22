package hello.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StatefulServiceTest {

    @Test
    void statefulServiceSingleton() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        // ThreadA: A사용자 10000원 주문
        int userAPrice = statefulService1.order("userA", 10000);

        // ThreadB: B사용자 20000원 주문
        int userBPrice = statefulService2.order("userB", 20000);

        // ThreadA: 사용자A 주문 금액 조회 -> 만원이 아니라 2만원으로 의도치 않게 나옴
        // int price = statefulService1.getPrice();

        assertThat(userAPrice).isNotSameAs(userBPrice);
    }

    static class TestConfig {

        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }
}