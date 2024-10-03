package com.example.Wanted.Market.API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class WantedMarketApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WantedMarketApiApplication.class, args);
	}
}
