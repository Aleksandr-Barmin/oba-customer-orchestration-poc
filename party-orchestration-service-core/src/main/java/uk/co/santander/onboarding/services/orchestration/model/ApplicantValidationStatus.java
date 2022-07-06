package uk.co.santander.onboarding.services.orchestration.model;

/**
 * Result of the applicant validation.
 */
public enum ApplicantValidationStatus {
    /**
     * Validation is successful.
     */
    SUCCESS(true),

    /**
     * Validation is not successful.
     */
    NOT_SUCCESS(false),

    /**
     * Validation status is unknown.
     */
    UNKNOWN(false);

    private final boolean positive;

    ApplicantValidationStatus(boolean positive) {
        this.positive = positive;
    }

    public boolean isPositive() {
        return positive;
    }
}
