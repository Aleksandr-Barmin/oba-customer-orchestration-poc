package uk.co.santander.onboarding.services.orchestration.service;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationEvent;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationState;

@Slf4j
@Component
public class StateMachineListener
    extends StateMachineListenerAdapter<OrchestrationState, OrchestrationEvent> {
  @Override
  public void transition(Transition<OrchestrationState, OrchestrationEvent> transition) {
    log.info(
        "Transition from {} to {} is happening",
        Optional.ofNullable(transition.getSource())
            .map(State::getId)
            .orElse(OrchestrationState.UNDEFINED),
        transition.getTarget().getId());
  }

  @Override
  public void stateEntered(State<OrchestrationState, OrchestrationEvent> state) {
    log.info("Entering {} state", state.getId());
  }

  @Override
  public void stateExited(State<OrchestrationState, OrchestrationEvent> state) {
    log.info("Exiting {} state", state.getId());
  }

  @Override
  public void transitionStarted(Transition<OrchestrationState, OrchestrationEvent> transition) {
    log.info("------------------------------------------------------------------------");
    log.info(
        "Transition from {} to {} started",
        Optional.ofNullable(transition.getSource())
            .map(State::getId)
            .orElse(OrchestrationState.UNDEFINED),
        transition.getTarget().getId());
  }

  @Override
  public void transitionEnded(Transition<OrchestrationState, OrchestrationEvent> transition) {
    log.info(
        "Transition from {} to {} ended",
        Optional.ofNullable(transition.getSource())
            .map(State::getId)
            .orElse(OrchestrationState.UNDEFINED),
        transition.getTarget().getId());
    log.info("------------------------------------------------------------------------");
  }

  @Override
  public void stateMachineError(
      StateMachine<OrchestrationState, OrchestrationEvent> stateMachine, Exception exception) {
    log.error("Exception was thrown during execution: ", exception);
  }
}
