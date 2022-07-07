package uk.co.santander.onboarding.services.orchestration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import uk.co.santander.onboarding.services.orchestration.config.WorldSimulatorConfig;

@SpringBootApplication
@EnableConfigurationProperties(WorldSimulatorConfig.class)
public class WorldSimulatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorldSimulatorApplication.class, args);
    }
}
