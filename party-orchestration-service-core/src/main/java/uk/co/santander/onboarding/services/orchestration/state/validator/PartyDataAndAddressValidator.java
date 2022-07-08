package uk.co.santander.onboarding.services.orchestration.state.validator;

import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.services.orchestration.client.core.DummyImplementation;
import uk.co.santander.onboarding.services.orchestration.model.ApplicantValidationResult;
import uk.co.santander.onboarding.services.orchestration.model.PartyDataAndAddress;

/** Validate information about applicant - personal and address data. */
@Component
@DummyImplementation
public class PartyDataAndAddressValidator {
  /**
   * Perform validation of the information of an applicant.
   *
   * @param data to validate.
   * @return validation result with status and some description like reasons of failed validation.
   */
  public ApplicantValidationResult validate(final PartyDataAndAddress data) {
    if (data.getApplicantOptional().isEmpty()) {
      return ApplicantValidationResult.noApplicant();
    }
    return ApplicantValidationResult.success();
  }
}
