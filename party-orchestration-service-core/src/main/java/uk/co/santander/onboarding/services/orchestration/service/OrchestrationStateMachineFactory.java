package uk.co.santander.onboarding.services.orchestration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationEvent;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationState;
import uk.co.santander.onboarding.services.orchestration.state.StateMachineDefinition;

/** Factory class to create state machines for customer on-boarding orchestration. */
@Component
public class OrchestrationStateMachineFactory {
  @Autowired private StateMachineFactory<OrchestrationState, OrchestrationEvent> springFactory;

  /**
   * Create and configure state machine.
   *
   * @return state machine instance.
   */
  public StateMachine<OrchestrationState, OrchestrationEvent> create() {
    final StateMachine<OrchestrationState, OrchestrationEvent> stateMachine =
        springFactory.getStateMachine(StateMachineDefinition.MACHINE_NAME);
    return stateMachine;
  }
}
