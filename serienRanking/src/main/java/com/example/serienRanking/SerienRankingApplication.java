package com.example.serienRanking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SerienRankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SerienRankingApplication.class, args);
	}

	@Bean
	public WebClient.Builder webClientBuilder() {
		return WebClient.builder();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://localhost:8081") // Annahme: Frontend läuft auf Port 8081
						.allowedMethods("GET", "POST", "PUT", "DELETE")
						.allowCredentials(true);
			}
		};
	}
}