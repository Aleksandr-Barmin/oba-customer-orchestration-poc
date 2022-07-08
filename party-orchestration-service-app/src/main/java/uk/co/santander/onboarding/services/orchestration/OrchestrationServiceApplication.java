package uk.co.santander.onboarding.services.orchestration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

/** Spring Boot application for the Customer On-boarding service.  */
@EnableCaching
@EnableFeignClients
@SpringBootApplication
public class OrchestrationServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(OrchestrationServiceApplication.class, args);
  }
}
