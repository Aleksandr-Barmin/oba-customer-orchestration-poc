package uk.co.santander.onboarding.services.orchestration.state.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.services.orchestration.service.ApplicationService;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationEvent;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationState;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateContextHelper;

import java.util.UUID;

@Component
public class OnGetApplicantDataFailure implements Action<OrchestrationState, OrchestrationEvent> {
    @Autowired
    private StateContextHelper helper;

    @Autowired
    private ApplicationService applicationService;

    @Override
    public void execute(StateContext<OrchestrationState, OrchestrationEvent> context) {
        final UUID applicationId = helper.getApplicationId(context);
        applicationService.createRecord(applicationId, "Applicant data was not received");
    }
}
