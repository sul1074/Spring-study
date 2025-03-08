package hello.hello_spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data", "hello!");

        /**
         * 문자를 리턴하면, viewResolver가 화면을 찾아서 처리해줌
         * resources:templates/ + {ViewName} + .html
         */
        return "hello";
    }

    // 요청할 때 들어온 name value가 String name 변수에 들어감
    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam(value="name") String name, Model model) {
        model.addAttribute("name", name);

        return "hello-template";
    }

    @GetMapping("hello-string")
    @ResponseBody    /**
                     * 뷰 파일을 찾지 않고, 메서드의 반환 값을 그대로 http 응답에 넣어주는 어노테이션
                     * 이때 반환값을 변환할 때, HttpMessageConverter가 JSON 혹은 String 형태로 변환해서 반환해줌
                     * 디폴트는 StringHttpMessageConverter이고, 객체라면 MappingJackson2HttpMessageConverter
                     */
    public String helloString(@RequestParam("name") String name) {
        return "hello " + name;
    }

    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);

        return hello; // {"name":"spring"} -> JSON
    }

    static class Hello {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
