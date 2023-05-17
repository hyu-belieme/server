package com.belieme.apiserver;

import com.belieme.apiserver.data.repository.custom.RefreshRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableJpaRepositories(repositoryBaseClass = RefreshRepositoryImpl.class)
public class BeliemeApiServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(BeliemeApiServerApplication.class, args);
  }
}
