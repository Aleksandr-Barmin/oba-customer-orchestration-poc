package uk.co.santander.onboarding.services.orchestration.state.guard;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.services.orchestration.model.ApplicantValidationResult;
import uk.co.santander.onboarding.services.orchestration.model.ApplicantValidationStatus;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationEvent;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationState;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateContextHelper;

@Component
public class ApplicantDataValidatedGuard implements Guard<OrchestrationState, OrchestrationEvent> {
  @Autowired private StateContextHelper helper;

  @Override
  public boolean evaluate(StateContext<OrchestrationState, OrchestrationEvent> context) {
    final ApplicantValidationResult validationResult =
        helper.getApplicationValidationResult(context);
    return Optional.ofNullable(validationResult)
        .map(ApplicantValidationResult::getStatus)
        .map(ApplicantValidationStatus::isPositive)
        .orElse(Boolean.FALSE);
  }
}
