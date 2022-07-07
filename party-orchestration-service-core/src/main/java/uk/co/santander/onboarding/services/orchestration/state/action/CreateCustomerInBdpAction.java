package uk.co.santander.onboarding.services.orchestration.state.action;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationEvent;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationState;

@Component
public class CreateCustomerInBdpAction implements Action<OrchestrationState, OrchestrationEvent> {
    @Override
    public void execute(StateContext<OrchestrationState, OrchestrationEvent> context) {

    }
}
