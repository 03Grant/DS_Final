package org.grant.server;

import org.grant.server.dto.serverConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class ServerApplication {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	public static void main(String[] args) {

		// 预处理，让服务器知道自己的IP
		serverConfiguration.getPublicIP();
		SpringApplication.run(ServerApplication.class, args);
	}
}
