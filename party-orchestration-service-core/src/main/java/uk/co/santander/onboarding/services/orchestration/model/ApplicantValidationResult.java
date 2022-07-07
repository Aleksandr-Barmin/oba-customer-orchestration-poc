package uk.co.santander.onboarding.services.orchestration.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Validation result with message.
 */
@Data
@RequiredArgsConstructor(staticName = "of")
public class ApplicantValidationResult {
    private final ApplicantValidationStatus status;
    private final String message;

    public boolean isUnknown() {
        return ApplicantValidationStatus.UNKNOWN == status || status == null;
    }

    public boolean isNegative() {
        return ApplicantValidationStatus.NOT_SUCCESS == status;
    }

    public boolean isPositive() {
        return ApplicantValidationStatus.SUCCESS == status;
    }

    public static ApplicantValidationResult noApplicant() {
        return ApplicantValidationResult.of(
                ApplicantValidationStatus.NOT_SUCCESS,
                "Can't retrieve applicant data"
        );
    }

    public static ApplicantValidationResult success() {
        return ApplicantValidationResult.of(
                ApplicantValidationStatus.SUCCESS,
                "Success validation"
        );
    }

    public static ApplicantValidationResult unknown() {
        return ApplicantValidationResult.of(
                ApplicantValidationStatus.UNKNOWN,
                "Status is unknown"
        );
    }
}
