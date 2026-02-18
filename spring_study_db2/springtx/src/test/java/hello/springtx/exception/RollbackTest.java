package hello.springtx.exception;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class RollbackTest {

    @Autowired
    RollbackService rollbackService;

    @Test
    void runtimeException() {
        Assertions.assertThatThrownBy(() -> rollbackService.runTimeException())
                        .isInstanceOf(RuntimeException.class);
    }

    @Test
    void checkedException() {
        Assertions.assertThatThrownBy(() -> rollbackService.checkedException())
                .isInstanceOf(MyException.class);
    }

    @Test
    void rollbackForCheckedException() {
        Assertions.assertThatThrownBy(() -> rollbackService.rollbackFor())
                .isInstanceOf(MyException.class);
    }

    @TestConfiguration
    static class RollbackTestConfig {

        @Bean
        RollbackService rollbackService() {
            return new RollbackService();
        }
    }

    @Slf4j
    static class RollbackService {

        // 런타임 예외: 롤백
        @Transactional
        public void runTimeException() {
            log.info("call runTimeException");
            throw new RuntimeException();
        }

        // 체크 예외: 커밋
        @Transactional
        public void checkedException() throws MyException {
            log.info("call checkedException");
            throw new MyException();
        }

        // 체크 예외 rollbackFor 지정: 롤백
        @Transactional(rollbackFor = MyException.class)
        public void rollbackFor() throws MyException {
            log.info("call rollbackFor checkedException");
            throw new MyException();
        }
    }

    static class MyException extends Exception {

    }
}


