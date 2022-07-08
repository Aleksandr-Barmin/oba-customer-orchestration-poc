package uk.co.santander.onboarding.services.orchestration.persistence;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import uk.co.santander.onboarding.services.orchestration.service.StateMachineRepository;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationEvent;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationState;

/**
 * Implementation of the state machine perist that stores information in memory. Can be used for
 * development and testing purposes only.
 */
public class InMemoryPersistence
    implements StateMachinePersist<OrchestrationState, OrchestrationEvent, UUID>,
        StateMachineRepository<OrchestrationState, OrchestrationEvent, UUID> {

  private final Map<UUID, StateMachineContext<OrchestrationState, OrchestrationEvent>> storage =
      new ConcurrentHashMap<>();

  @Override
  public boolean isAvailable(UUID applicationId) {
    return storage.containsKey(applicationId);
  }

  @Override
  public void write(
      StateMachineContext<OrchestrationState, OrchestrationEvent> context, UUID contextObj)
      throws Exception {
    storage.put(contextObj, context);
  }

  @Override
  public StateMachineContext<OrchestrationState, OrchestrationEvent> read(UUID contextObj)
      throws Exception {
    return storage.get(contextObj);
  }
}
