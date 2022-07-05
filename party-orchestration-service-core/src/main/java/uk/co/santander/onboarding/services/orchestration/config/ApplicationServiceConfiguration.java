package uk.co.santander.onboarding.services.orchestration.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.santander.onboarding.services.orchestration.persistence.InMemoryApplicationRepository;
import uk.co.santander.onboarding.services.orchestration.service.ApplicationRepository;

@Configuration
public class ApplicationServiceConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ApplicationRepository applicationRepository() {
        return new InMemoryApplicationRepository();
    }
}
