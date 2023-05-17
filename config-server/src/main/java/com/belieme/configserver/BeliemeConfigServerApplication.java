package com.belieme.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class BeliemeConfigServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(BeliemeConfigServerApplication.class, args);
  }

}
