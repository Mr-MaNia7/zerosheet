package com.mania.zerosheet;

import com.mania.zerosheet.Items.Status;
import com.mania.zerosheet.Transaction.Transaction;
import com.mania.zerosheet.Transaction.TransactionRepository;
import com.mania.zerosheet.Items.Item;
import com.mania.zerosheet.Items.ItemRepository;

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
	public CommandLineRunner dataSeeder(ItemRepository repo, TransactionRepository trepo){
		return args -> {
			repo.save(new Item("Phone","PCS", 2000, 22, Status.AVAILABLE));
			trepo.save(new Transaction("test1"));
		};
	}
}
