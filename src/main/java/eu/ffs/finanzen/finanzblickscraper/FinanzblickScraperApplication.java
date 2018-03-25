package eu.ffs.finanzen.finanzblickscraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FinanzblickScraperApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanzblickScraperApplication.class, args);
	}
}
