package hello.springmvc.basic.request;

import hello.springmvc.basic.HelloData;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Slf4j
@Controller
public class RequestParamController {

    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        log.info("username = {}, age = {}", username, age);

        PrintWriter w = response.getWriter();
        w.write("ok");
    }

    @ResponseBody // 반환하는 문자열이 논리 경로인 viewName이 아니고, 그냥 http 응답 바디에 문자열 자체를 반환
    @RequestMapping("/request-param-v2")
    public String requestParamV2(@RequestParam("username") String username,
                                 @RequestParam("age") int age) {

        log.info("username = {}, age = {}", username, age);

        return "ok";
    }

    // 버전 이슈와, 비직관적이라서 잘 사용 X
//    @ResponseBody
//    @RequestMapping("/request-param-v3")
//    public String requestParamV3(@RequestParam String username,
//                                 @RequestParam int age) {
//
//        log.info("username = {}, age = {}", username, age);
//
//        return "ok";
//    }

    // 버전 이슈와, 비직관적이라서 잘 사용 X
//    @ResponseBody
//    @RequestMapping("/request-param-v4")
//    public String requestParamV4(String username, int age) {
//        log.info("username = {}, age = {}", username, age);
//
//        return "ok";
//    }

    // username = 인 경우, 빈 문자가 들어와버림. 즉, 오류가 발생 안해버림.
    @ResponseBody
    @RequestMapping("/request-param-required") // username 파라미터가 무조건 들어와야 함
    public String requestParamRequired(@RequestParam(value = "username", required = true) String username,
                                       @RequestParam(value = "age", required = false) Integer age) {

        log.info("username = {}, age = {}", username, age);

        return "ok";
    }

    // 파라미터 안들어오면, 디폴트 값으로 대체. username = 인(빈 문자) 경우에도 디폴트로 값 넣어줌. 굿.
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(@RequestParam(value = "username", defaultValue = "guest") String username,
                                      @RequestParam(value = "age", defaultValue = "-1") int age) {

        log.info("username = {}, age = {}", username, age);

        return "ok";
    }

    // 요청 파라미터 전부 가져오기.
    // 파라미터의 값이 중복될 수 있을 것 같으면, MultiValueMap을 사용하자.
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap) {

        log.info("username = {}, age = {}", paramMap.get("username"), paramMap.get("age"));

        return "ok";
    }

    // 마법처럼 HelloData 객체가 생성되고, 요청 파라미터 값도 모두 들어가 있음 -> 프로퍼티
    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(@ModelAttribute HelloData helloData) {
        log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    // 심지어 생략해도 잘 됨
    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(HelloData helloData) {
        log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }
}