package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        // AppConfig 등 현재 프로젝트에서는 수동 등록된 정보를 자동으로 등록되게 하면 안되므로 제외
        // 실제로는 같이 사용하기도 함
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class),
        basePackages = "hello.core" // 탐색을 시작할 위치 설정
)
public class AutoAppConfig {
}
