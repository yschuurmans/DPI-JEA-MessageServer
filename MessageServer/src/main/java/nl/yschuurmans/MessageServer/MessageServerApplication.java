package nl.yschuurmans.MessageServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MessageServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessageServerApplication.class, args);
	}
}
