package uk.co.santander.onboarding.services.orchestration.config;

import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import uk.co.santander.onboarding.services.orchestration.persistence.InMemoryPersistence;
import uk.co.santander.onboarding.services.orchestration.service.StateMachineRepository;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationEvent;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationState;

@Configuration
public class OrchestrationServiceConfiguration {
  @Bean
  public StateMachinePersister<OrchestrationState, OrchestrationEvent, UUID> persister(
      StateMachinePersist<OrchestrationState, OrchestrationEvent, UUID> storage) {
    return new DefaultStateMachinePersister<>(storage);
  }

  @Bean
  @ConditionalOnMissingBean
  public StateMachinePersist<OrchestrationState, OrchestrationEvent, UUID> persist() {
    return new InMemoryPersistence();
  }

  @Bean
  @ConditionalOnMissingBean
  public StateMachineRepository<OrchestrationState, OrchestrationEvent, UUID> repository(
      StateMachinePersist<OrchestrationState, OrchestrationEvent, UUID> storage) {
    if (storage instanceof StateMachineRepository) {
      return StateMachineRepository.class.cast(storage);
    }
    throw new RuntimeException("Declare a bean separately");
  }
}
