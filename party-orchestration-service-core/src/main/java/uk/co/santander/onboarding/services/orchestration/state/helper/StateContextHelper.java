package uk.co.santander.onboarding.services.orchestration.state.helper;

import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationEvent;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationState;

import java.util.UUID;

@Component
public class StateContextHelper {
    public UUID getApplicationId(StateContext<OrchestrationState, OrchestrationEvent> context) {
        return (UUID) context.getExtendedState().getVariables().get("applicationId");
    }

    public void setApplicationId(StateContext<OrchestrationState, OrchestrationEvent> context, UUID applicationId) {
        context.getExtendedState().getVariables().put("applicationId", applicationId);
    }
}
