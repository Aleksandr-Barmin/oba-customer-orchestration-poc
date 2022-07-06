package uk.co.santander.onboarding.services.orchestration.state.action;

import com.google.common.collect.Maps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.co.santander.onboarding.services.orchestration.client.PartyAddressServiceClient;
import uk.co.santander.onboarding.services.orchestration.client.PartyDataServiceClient;
import uk.co.santander.onboarding.services.orchestration.model.ApplicantValidationResult;
import uk.co.santander.onboarding.services.orchestration.model.ApplicantValidationStatus;
import uk.co.santander.onboarding.services.orchestration.service.ApplicationService;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateConstants;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateContextHelper;
import uk.co.santander.onboarding.services.orchestration.state.validator.PartyDataAndAddress;
import uk.co.santander.onboarding.services.orchestration.state.validator.PartyDataAndAddressValidator;
import uk.co.santander.onboarding.services.party.dto.ApplicantDTO;
import uk.co.santander.onboarding.services.party.dto.ContactPointDTO;
import uk.co.santander.onboarding.services.party.dto.PostalAddressesDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        StateContextHelper.class,
        GetAndVerifyApplicantDataAction.class
})
class GetAndVerifyApplicantDataActionTest {
    @Autowired
    private GetAndVerifyApplicantDataAction uut;

    @MockBean
    private ApplicationService applicationService;

    @MockBean
    private PartyDataServiceClient dataServiceClient;

    @MockBean
    private PartyAddressServiceClient addressServiceClient;

    @MockBean
    private PartyDataAndAddressValidator validator;

    @Mock
    private StateContext stateContext;

    @Mock
    private ExtendedState extendedState;

    @Test
    @DisplayName("Context should start")
    void check_contextStarts() {
        assertThat(uut).isNotNull()
                .withFailMessage("Context does not start");
    }

    @Test
    @DisplayName("Should throw an exception when Application ID is not provided")
    void execute_shouldThrowExceptionWhenApplicationIdNotProvided() {
        when(stateContext.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(Map.of());

        assertThatThrownBy(() -> {
            uut.execute(stateContext);
        })
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Application ID should be provided");
    }

    @Test
    @DisplayName("Should create a record when data retrieval starts")
    void execute_shouldCreateRecordWhenStarted() {
        final UUID applicationId = UUID.randomUUID();

        when(stateContext.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(Maps.newHashMap(Map.of(
                StateConstants.APPLICATION_ID, applicationId
        )));

        uut.execute(stateContext);

        verify(applicationService, times(1)).createRecord(
                eq(applicationId),
                contains("Start getting")
        );
    }

    @Test
    @DisplayName("Should request data from party data and address services via client")
    void execute_shouldRequestDataFromPartyDataService() {
        final UUID applicationId = UUID.randomUUID();
        final UUID postalAddressId = UUID.randomUUID();

        when(stateContext.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(Maps.newHashMap(Map.of(
                StateConstants.APPLICATION_ID, applicationId
        )));

        when(dataServiceClient.findById(eq(applicationId))).thenReturn(Optional.of(
                ApplicantDTO.builder()
                        .contactPoint(ContactPointDTO.builder()
                                .postalAddresses(List.of(
                                        PostalAddressesDTO.builder()
                                                .addressId(postalAddressId)
                                                .build()
                                ))
                                .build())
                        .build()
        ));

        when(validator.validate(any(PartyDataAndAddress.class))).thenReturn(ApplicantValidationResult.unknown());

        uut.execute(stateContext);

        verify(dataServiceClient, times(1)).findById(eq(applicationId));
        verify(addressServiceClient, times(1)).findById(postalAddressId);
    }

    @Test
    @DisplayName("Should save invalid status when applicant is not available")
    void execute_shouldSaveResultWhenApplicantIsNotAvailable() {
        final UUID applicationId = UUID.randomUUID();

        when(stateContext.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(Maps.newHashMap(Map.of(
                StateConstants.APPLICATION_ID, applicationId
        )));

        when(dataServiceClient.findById(eq(applicationId))).thenReturn(Optional.empty());

        uut.execute(stateContext);

        verify(dataServiceClient, times(1)).findById(eq(applicationId));
        verify(addressServiceClient, never()).findById(any(UUID.class));

        final ApplicantValidationStatus result = (ApplicantValidationStatus) stateContext.getExtendedState()
                .getVariables()
                .get(StateConstants.APPLICANT_VALIDATION_STATUS);

        assertThat(result)
                .isEqualTo(ApplicantValidationStatus.NOT_SUCCESS)
                .withFailMessage("Invalid validation status");

        verify(applicationService, times(1)).createRecord(
                eq(applicationId),
                contains("Data not received from party data service")
        );
    }

    @ParameterizedTest(name = "Checking {0}")
    @EnumSource(ApplicantValidationStatus.class)
    @DisplayName("Should invoke validator and save the result")
    void execute_shouldInvokeValidator(final ApplicantValidationStatus validatorResponse) {
        final UUID applicationId = UUID.randomUUID();
        final UUID postalAddressId = UUID.randomUUID();

        when(stateContext.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(Maps.newHashMap(Map.of(
                StateConstants.APPLICATION_ID, applicationId
        )));

        when(dataServiceClient.findById(eq(applicationId))).thenReturn(Optional.of(
                ApplicantDTO.builder()
                        .contactPoint(ContactPointDTO.builder()
                                .postalAddresses(List.of(
                                        PostalAddressesDTO.builder()
                                                .addressId(postalAddressId)
                                                .build()
                                ))
                                .build())
                        .build()
        ));

        when(validator.validate(any(PartyDataAndAddress.class))).thenReturn(ApplicantValidationResult.of(
                validatorResponse,
                ""
        ));

        uut.execute(stateContext);

        verify(validator, times(1)).validate(any(PartyDataAndAddress.class));

        final ApplicantValidationStatus result = (ApplicantValidationStatus) stateContext.getExtendedState()
                .getVariables()
                .get(StateConstants.APPLICANT_VALIDATION_STATUS);

        assertThat(result)
                .isEqualTo(validatorResponse)
                .withFailMessage("Invalid validator response");

        verify(applicationService, times(1)).createRecord(
                eq(applicationId),
                contains("Validation result is")
        );
    }
}