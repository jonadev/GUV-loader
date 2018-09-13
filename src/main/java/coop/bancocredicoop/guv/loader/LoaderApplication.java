package coop.bancocredicoop.guv.loader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LoaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoaderApplication.class, args);
	}
}
