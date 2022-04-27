package com.mania.zerosheet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ZerosheetApplication {
	public static void main(String[] args) {
		SpringApplication.run(ZerosheetApplication.class, args);
	}
	@Bean
	public CommandLineRunner dataSeeder(ItemRepository repo){
		return args -> {
			repo.save(new Item(14L, "Phone", Category.MUSIC));
		};
	}
}
