package uk.co.santander.onboarding.services.orchestration.state.validator;

import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.services.orchestration.client.core.DummyImplementation;
import uk.co.santander.onboarding.services.orchestration.model.ApplicantValidationResult;
import uk.co.santander.onboarding.services.orchestration.model.PartyDataAndAddress;

@Component
@DummyImplementation
public class PartyDataAndAddressValidator {
  public ApplicantValidationResult validate(final PartyDataAndAddress data) {
    if (data.getApplicantOptional().isEmpty()) {
      return ApplicantValidationResult.noApplicant();
    }
    return ApplicantValidationResult.success();
  }
}
