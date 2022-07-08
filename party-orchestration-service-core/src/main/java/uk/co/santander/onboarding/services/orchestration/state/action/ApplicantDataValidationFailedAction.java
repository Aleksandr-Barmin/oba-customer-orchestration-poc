package uk.co.santander.onboarding.services.orchestration.state.action;

import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.services.orchestration.model.ApplicantValidationResult;
import uk.co.santander.onboarding.services.orchestration.service.ApplicationService;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationEvent;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationState;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateContextHelper;

@Component
public class ApplicantDataValidationFailedAction
    implements Action<OrchestrationState, OrchestrationEvent> {
  @Autowired private StateContextHelper helper;

  @Autowired private ApplicationService applicationService;

  @Override
  public void execute(StateContext<OrchestrationState, OrchestrationEvent> context) {
    final UUID applicationId = helper.getApplicationId(context);
    final ApplicantValidationResult validationResult =
        helper.getApplicationValidationResult(context);

    if (Objects.isNull(validationResult) || validationResult.isUnknown()) {
      throw new IllegalStateException("Validation result should be in the context");
    }

    if (!validationResult.isNegative()) {
      throw new IllegalStateException("Validation result should be negative");
    }

    applicationService.createRecord(
        applicationId,
        String.format("Failed due to the validation error: %s", validationResult.getMessage()));
  }
}
