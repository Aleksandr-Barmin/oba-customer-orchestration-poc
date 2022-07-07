package uk.co.santander.onboarding.services.orchestration.state.guard;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationEvent;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationState;

@Component
public class CustomerNotFoundInBDPGuard implements Guard<OrchestrationState, OrchestrationEvent> {
    @Override
    public boolean evaluate(StateContext<OrchestrationState, OrchestrationEvent> context) {
        // TODO: write code here :)
        throw new UnsupportedOperationException();
    }
}
