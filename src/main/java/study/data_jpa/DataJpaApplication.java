package study.data_jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider() {
		//테스트 상황이기 때문에 uuid를 이용했지만, 실무에서는
		// HttpSession에서 user id 를 받아오거나 하는 방식으로 uuid 대신에 입력해야한다.
		return () -> Optional.of(UUID.randomUUID().toString());
	}
}
