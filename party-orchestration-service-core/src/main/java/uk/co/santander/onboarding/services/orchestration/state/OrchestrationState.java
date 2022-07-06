package uk.co.santander.onboarding.services.orchestration.state;

public enum OrchestrationState {
    /**
     * SM in this state when it is created.
     */
    MACHINE_CREATED,

    /**
     * The first working state - up to this state, the machine is initialized,
     * necessary records are created in the DB.
     */
    MACHINE_INITIALIZED,

    /**
     * Getting an applicant from the party data service.
     */
    GET_APPLICANT_DATA_AND_VALIDATE_STATE,

    /**
     * Applicant's data not validated successfully.
     */
    APPLICANT_DATA_VALIDATION_FAILED_STATE,

    UNDEFINED
}
