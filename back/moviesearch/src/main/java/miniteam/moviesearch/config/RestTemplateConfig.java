package miniteam.moviesearch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


// RestTemplate 객체를 Spring Bean으로 등록해서 서비스에서 의존성 주입(@Autowired, @RequiredArgsConstructor)으로 사용할 수 있게 해줌
// 한 번만 설정해두면 프로젝트 어디서든 RestTemplate사용 가능해지기 때문에 깔끔하고 효율적임
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
