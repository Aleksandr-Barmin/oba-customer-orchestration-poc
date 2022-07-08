package uk.co.santander.onboarding.services.orchestration.service;

import java.util.UUID;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationEvent;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationState;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateContextHelper;

/** Service to interact with state machines. */
@Service
public class StateMachineService {
  @Autowired
  private StateMachineRepository<OrchestrationState, OrchestrationEvent, UUID> repository;

  @Autowired private StateMachinePersister<OrchestrationState, OrchestrationEvent, UUID> persister;

  @Autowired private OrchestrationStateMachineFactory stateMachineFactory;

  @Autowired private StateContextHelper contextHelper;

  /**
   * Send an event to the state machine with given identifier.
   *
   * @param applicationId identifier of a state machine.
   * @param event to be sent to the state machine.
   * @return current (final?) state of a state machine.
   */
  public OrchestrationState sendEvent(UUID applicationId, OrchestrationEvent event) {
    return withMachine(
        applicationId,
        (machine) -> {
          machine.sendEvent(event);
        });
  }

  @SneakyThrows
  private OrchestrationState withMachine(UUID applicationId, ExecutionCallback callback) {
    final StateMachine<OrchestrationState, OrchestrationEvent> stateMachine =
        getStateMachine(applicationId);
    callback.execute(stateMachine);
    persister.persist(stateMachine, applicationId);
    // need to return the current state after all
    return stateMachine.getState().getId();
  }

  @SneakyThrows
  private StateMachine<OrchestrationState, OrchestrationEvent> getStateMachine(UUID applicationId) {
    final StateMachine<OrchestrationState, OrchestrationEvent> stateMachine =
        stateMachineFactory.create();
    if (repository.isAvailable(applicationId)) {
      persister.restore(stateMachine, applicationId);
      return stateMachine;
    }
    // TODO: wrap with some meaningful helper
    stateMachine.getExtendedState().getVariables().put("applicationId", applicationId);
    stateMachine.start();
    return stateMachine;
  }

  interface ExecutionCallback {
    void execute(StateMachine<OrchestrationState, OrchestrationEvent> machine);
  }
}
