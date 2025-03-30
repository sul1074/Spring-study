package hello.springmvc.basic;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j // private final Logger log = LoggerFactory.getLogger(this.getClass()); 자동으로 넣어줌
@RestController // RestController에서 반환하는 문자열은 뷰 이름이 아니라,그냥 문자열 그대로 반환
public class LogTestController {

    // private final Logger log = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/log-test")
    public String logTest() {
        String name = "Spring";

        // 로컬 PC에서는 이정도 레벨로(모든 로그 출력). logging.level.hello.springmvc=trace
        log.trace("trace log = {}", name);

        // 개발 서버에서는 이정도 레벨로. logging.level.hello.springmvc=debug
        log.debug("debug log = {}", name);

        // 운영 서버에서 이정도 레벨로. 디폴트가 이 수준. logging.level.hello.springmvc=info
        log.info(" info log = {}", name);
        log.warn(" warn log = {}", name);
        log.error(" error log = {}", name);

        return "ok";
    }
}
