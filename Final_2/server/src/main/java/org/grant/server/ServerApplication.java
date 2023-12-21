package org.grant.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ServerApplication {
	@Bean
	public ContactManager contactManager() {return new ContactManager();}
	@Bean
	public MembershipManager membershipManager() {
		return new MembershipManager();
	}


	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
