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
    GET_APPLICANT_DATA_STATE,

    /**
     * The system did not manage to get an applicant from the party data service.
     */
    GET_APPLICANT_DATA_FAILURE_STATE,

    /**
     * Applicant is retrieved, validating.
     */
    VALIDATE_APPLICANT_DATA_STATE,

    UNDEFINED
}
