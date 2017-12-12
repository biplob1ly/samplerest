package com.plethora.samplerest;

import com.plethora.samplerest.Entity.Account;
import com.plethora.samplerest.Entity.Bookmark;
import com.plethora.samplerest.Repository.AccountRepository;
import com.plethora.samplerest.Repository.BookmarkRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class SamplerestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SamplerestApplication.class, args);
	}

	@Bean
	CommandLineRunner init(AccountRepository accountRepository, BookmarkRepository bookmarkRepository) {
		return (evt) -> Arrays.asList("biplob,arup,moumita,tupki,utpal".split(",")).forEach(
				a -> {
					Account account = accountRepository.save(new Account(a, "password"));
					bookmarkRepository.save(new Bookmark(account, "http://bookmark.com/1/" + a, "A description"));
					bookmarkRepository.save(new Bookmark(account, "http://bookmark.com/2/" + a, "Another description"));
				}
		);
	}
}
