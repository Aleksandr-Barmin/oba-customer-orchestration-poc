package uk.co.santander.onboarding.services.orchestration.state.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.services.orchestration.model.ApplicantValidationResult;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationEvent;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationState;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateContextHelper;

import java.util.Objects;
import java.util.UUID;

@Component
public class ValidateAndSearchCustomerInBdpAction implements Action<OrchestrationState, OrchestrationEvent> {
    @Autowired
    private StateContextHelper helper;

    @Override
    public void execute(StateContext<OrchestrationState, OrchestrationEvent> context) {
        final UUID applicationId = helper.getApplicationId(context);
        Objects.requireNonNull(applicationId, "Application ID should be provided");

        final ApplicantValidationResult validationResult = helper.getApplicationValidationResult(context);
        if (!validationResult.isPositive()) {
            throw new IllegalStateException("Validation result should be positive");
        }


    }
}
