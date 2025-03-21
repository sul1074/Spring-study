package hello.hello_spring.AOP;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 공통 관심 사항과 핵심 관심 사항을 분리하고 싶을 떄 AOP를 사용
 * ex 모든 메서드의 호출 시간 측정
 * 컨트롤러가 서비스에 접근할 때, 가짜 서비스인 프록시를 생성해서 접근하게 함
 */

@Aspect
@Component // 컴퍼넌트 스캔
public class TimeTraceApp {

    // 하위 패키지에 대해 모두 적용 가능
    @Around("execution(* hello.hello_spring..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("START: " + joinPoint.toString());
        try {
            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("END: " + joinPoint.toString() + " " + timeMs + "ms");

        }
    }
}
