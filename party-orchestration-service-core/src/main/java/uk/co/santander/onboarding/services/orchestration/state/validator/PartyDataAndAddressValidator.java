package uk.co.santander.onboarding.services.orchestration.state.validator;

import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.services.orchestration.model.PartyDataAndAddress;
import uk.co.santander.onboarding.services.orchestration.model.ApplicantValidationResult;
import uk.co.santander.onboarding.services.orchestration.model.ApplicantValidationStatus;

@Component
public class PartyDataAndAddressValidator {
    public ApplicantValidationResult validate(final PartyDataAndAddress data) {
        return ApplicantValidationResult.of(
                ApplicantValidationStatus.UNKNOWN,
                ""
        );
    }
}
