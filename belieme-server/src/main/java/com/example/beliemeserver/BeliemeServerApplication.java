package com.example.beliemeserver;

import com.example.beliemeserver.data.entity.old.OldStuffEntity;
import com.example.beliemeserver.data.repository.old.OldHistoryRepository;
import com.example.beliemeserver.data.repository.old.OldItemRepository;
import com.example.beliemeserver.data.repository.old.OldStuffRepository;
import com.example.beliemeserver.data.repository.old.OldUserRepository;
import com.example.beliemeserver.data.repository.custom.RefreshRepositoryImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Iterator;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = RefreshRepositoryImpl.class)
public class BeliemeServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(BeliemeServerApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(OldUserRepository userRepository, OldStuffRepository stuffRepository, OldItemRepository itemRepository, OldHistoryRepository historyRepository) throws Exception {
		return (String[] args) -> {
			int maxId = 0;
			Iterator<OldStuffEntity> iterator = stuffRepository.findAll().iterator();
			while(iterator.hasNext()) {
				OldStuffEntity tmp = iterator.next();
				if (tmp.getId() > maxId) {
					maxId = tmp.getId();
				}
			}
			System.out.println("#######run###### maxId : " + maxId);
			OldStuffEntity.setCounter(maxId+1);
		};
	}
}
