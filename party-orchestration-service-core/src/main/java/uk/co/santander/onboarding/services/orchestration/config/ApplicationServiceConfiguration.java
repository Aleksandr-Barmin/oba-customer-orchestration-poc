package uk.co.santander.onboarding.services.orchestration.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.santander.onboarding.services.orchestration.persistence.InMemoryApplicationRepository;
import uk.co.santander.onboarding.services.orchestration.service.ApplicationRepository;

/**
 * Configuration for the application records repository. This repository stores information about
 * business events happened during application.
 */
@Configuration(proxyBeanMethods = false)
public final class ApplicationServiceConfiguration {
    /**
     * Declaring a bean which actually stores information about application business events.
     *
     * @return application repository bean.
     */
    @Bean
    @ConditionalOnMissingBean
    public ApplicationRepository applicationRepository() {
        return new InMemoryApplicationRepository();
    }
}
