package drajner.hetman;

import drajner.hetman.requests.Person;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HetmanApplication {

	@SneakyThrows
	public static void main(String[] args) {

		SpringApplication.run(HetmanApplication.class, args);
	}

}
