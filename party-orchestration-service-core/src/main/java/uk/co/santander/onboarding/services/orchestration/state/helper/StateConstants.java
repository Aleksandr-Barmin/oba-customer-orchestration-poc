package uk.co.santander.onboarding.services.orchestration.state.helper;

/**
 * Constants for fields in the state machine context. I assume, it would be better to replace it
 * with POJO next.
 */
public interface StateConstants {
    String APPLICATION_ID = "applicationId";
    String APPLICANT_VALIDATION_STATUS = "applicantValidationStatus";
    String APPLICANT_VALIDATION_MESSAGE = "applicantValidationMessage";
    String CORE_SEARCH_STATUS = "coreSearchStatus";
    String CORE_SEARCH_F_NUMBER = "coreSearchFNumber";
    String CORE_SEARCH_BDP_UUID = "coreSearchBDPUUID";
    String CORE_CREATE_F_NUMBER = "coreCreateFNumber";
    String CORE_CREATE_DBP_UUID = "createCreateBDPUUID";
}
