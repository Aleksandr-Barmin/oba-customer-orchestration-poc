package uk.co.santander.onboarding.services.orchestration.state.helper;

import java.util.Objects;
import java.util.UUID;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.core.client.search.CustomerSearchStatus;
import uk.co.santander.onboarding.services.orchestration.model.ApplicantValidationResult;
import uk.co.santander.onboarding.services.orchestration.model.ApplicantValidationStatus;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationEvent;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationState;

@Component
public class StateContextHelper {
  public void setCustomerSearchStatus(
      final StateContext<OrchestrationState, OrchestrationEvent> context,
      final CustomerSearchStatus status) {
    Objects.requireNonNull(context, "Context should not be null");
    Objects.requireNonNull(status, "Status should not be null");

    write(context, StateConstants.CORE_SEARCH_STATUS, status);
  }

  public CustomerSearchStatus getCustomerSearchStatus(
      final StateContext<OrchestrationState, OrchestrationEvent> context) {
    Objects.requireNonNull(context, "Context should not be null");

    return read(context, StateConstants.CORE_SEARCH_STATUS, CustomerSearchStatus.class);
  }

  public ApplicantValidationResult getApplicationValidationResult(
      final StateContext<OrchestrationState, OrchestrationEvent> context) {
    Objects.requireNonNull(context, "Context should not be null");

    return ApplicantValidationResult.of(
        read(context, StateConstants.APPLICANT_VALIDATION_STATUS, ApplicantValidationStatus.class),
        read(context, StateConstants.APPLICANT_VALIDATION_MESSAGE, String.class));
  }

  public void setApplicantValidationResult(
      final StateContext<OrchestrationState, OrchestrationEvent> context,
      final ApplicantValidationResult validationInfo) {

    Objects.requireNonNull(context, "Context should not be null");
    Objects.requireNonNull(validationInfo, "Result should not be null");

    write(context, StateConstants.APPLICANT_VALIDATION_STATUS, validationInfo.getStatus());
    write(context, StateConstants.APPLICANT_VALIDATION_MESSAGE, validationInfo.getMessage());
  }

  public UUID getApplicationId(StateContext<OrchestrationState, OrchestrationEvent> context) {
    return read(context, StateConstants.APPLICATION_ID, UUID.class);
  }

  public void setApplicationId(
      StateContext<OrchestrationState, OrchestrationEvent> context, UUID applicationId) {
    write(context, StateConstants.APPLICATION_ID, applicationId);
  }

  private <T> T read(
      final StateContext<OrchestrationState, OrchestrationEvent> context,
      final String field,
      final Class<T> targetClass) {

    return (T) context.getExtendedState().getVariables().get(field);
  }

  private void write(
      final StateContext<OrchestrationState, OrchestrationEvent> context,
      final String field,
      final Object value) {

    context.getExtendedState().getVariables().put(field, value);
  }
}
