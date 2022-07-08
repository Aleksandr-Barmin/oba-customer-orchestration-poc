package uk.co.santander.onboarding.services.orchestration.state;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.test.StateMachineTestPlan;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.co.santander.onboarding.core.client.create.CustomerCreateClient;
import uk.co.santander.onboarding.core.client.create.CustomerCreateRequest;
import uk.co.santander.onboarding.core.client.create.CustomerCreateResponse;
import uk.co.santander.onboarding.core.client.search.CustomerSearchClient;
import uk.co.santander.onboarding.core.client.search.CustomerSearchRequest;
import uk.co.santander.onboarding.core.client.search.CustomerSearchResponse;
import uk.co.santander.onboarding.core.client.search.CustomerSearchStatus;
import uk.co.santander.onboarding.services.orchestration.client.baas.PartyAddressServiceClient;
import uk.co.santander.onboarding.services.orchestration.client.baas.PartyDataFacade;
import uk.co.santander.onboarding.services.orchestration.client.baas.PartyDataServiceClient;
import uk.co.santander.onboarding.services.orchestration.client.core.CustomerCreateRequestAdapter;
import uk.co.santander.onboarding.services.orchestration.client.core.CustomerSearchRequestAdapter;
import uk.co.santander.onboarding.services.orchestration.model.ApplicantValidationResult;
import uk.co.santander.onboarding.services.orchestration.model.ApplicantValidationStatus;
import uk.co.santander.onboarding.services.orchestration.model.PartyDataAndAddress;
import uk.co.santander.onboarding.services.orchestration.service.ApplicationService;
import uk.co.santander.onboarding.services.orchestration.service.OrchestrationStateMachineFactory;
import uk.co.santander.onboarding.services.orchestration.service.StateMachineListener;
import uk.co.santander.onboarding.services.orchestration.state.action.ApplicantDataValidationFailedAction;
import uk.co.santander.onboarding.services.orchestration.state.action.CreateCustomerInBdpAction;
import uk.co.santander.onboarding.services.orchestration.state.action.GetAndVerifyApplicantDataAction;
import uk.co.santander.onboarding.services.orchestration.state.action.OnMachineInitialization;
import uk.co.santander.onboarding.services.orchestration.state.action.SearchCustomerInBdpAction;
import uk.co.santander.onboarding.services.orchestration.state.guard.ApplicantDataValidatedGuard;
import uk.co.santander.onboarding.services.orchestration.state.guard.CustomerNotFoundInBdpGuard;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateConstants;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateContextHelper;
import uk.co.santander.onboarding.services.orchestration.state.validator.PartyDataAndAddressValidator;
import uk.co.santander.onboarding.services.party.dto.ApplicantDTO;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        StateMachineListener.class,
        StateMachineDefinition.class,

        OnMachineInitialization.class,
        GetAndVerifyApplicantDataAction.class,
        ApplicantDataValidationFailedAction.class,
        ApplicantDataValidatedGuard.class,
        SearchCustomerInBdpAction.class,
        CustomerNotFoundInBdpGuard.class,
        CreateCustomerInBdpAction.class,

        CustomerSearchRequestAdapter.class,
        CustomerCreateRequestAdapter.class,

        StateContextHelper.class,
        PartyDataFacade.class,

        OrchestrationStateMachineFactory.class
})
class StateMachineDefinitionTest {
    @Autowired
    private OrchestrationStateMachineFactory factory;

    @MockBean
    private ApplicationService applicationService;

    @MockBean
    private PartyDataServiceClient dataServiceClient;

    @MockBean
    private PartyAddressServiceClient addressServiceClient;

    @MockBean
    private CustomerSearchClient customerSearchClient;

    @MockBean
    private CustomerCreateClient customerCreateClient;

    @MockBean
    private PartyDataAndAddressValidator partyDataValidator;

    @Test
    @DisplayName("Context should start")
    void context_shouldStart() {
        assertThat(factory).isNotNull()
                .withFailMessage("Context should start");

        final StateMachine<OrchestrationState, OrchestrationEvent> stateMachine = factory.create();
        assertThat(stateMachine).isNotNull()
                .withFailMessage("State machine should be created");
    }

    private StateMachine<OrchestrationState, OrchestrationEvent> buildStateMachine(final UUID applicantId) {
        final StateMachine<OrchestrationState, OrchestrationEvent> stateMachine = factory.create();
        stateMachine.getExtendedState().getVariables().put(
                StateConstants.APPLICATION_ID,
                applicantId
        );
        return stateMachine;
    }

    @Test
    @DisplayName("Applicant ID should be saved to the context")
    void execute_applicationIdShouldBeSavedToContext() throws Exception {
        final UUID applicantId = UUID.randomUUID();
        final StateMachine<OrchestrationState, OrchestrationEvent> stateMachine = buildStateMachine(applicantId);

        final StateMachineTestPlan<OrchestrationState, OrchestrationEvent> testPlan = StateMachineTestPlanBuilder.<OrchestrationState, OrchestrationEvent>builder()
                .stateMachine(stateMachine)
                .step()
                    .expectStateEntered(OrchestrationState.MACHINE_CREATED)
                    .expectVariable(
                            StateConstants.APPLICATION_ID,
                            applicantId
                    )
                    .and()
                .build();

        testPlan.test();
    }

    @Test
    @DisplayName("Should stop when party data not available")
    void execute_shouldStopWhenPartyDataNotAvailable() throws Exception {
        final UUID applicantId = UUID.randomUUID();
        final StateMachine<OrchestrationState, OrchestrationEvent> stateMachine = buildStateMachine(applicantId);

        final StateMachineTestPlan<OrchestrationState, OrchestrationEvent> testPlan = StateMachineTestPlanBuilder.<OrchestrationState, OrchestrationEvent>builder()
                .stateMachine(stateMachine)
                .step()
                    .sendEvent(OrchestrationEvent.START_EXECUTION)
                    .expectState(OrchestrationState.APPLICANT_DATA_VALIDATION_FAILED_STATE)
                    .expectVariable(
                            StateConstants.APPLICANT_VALIDATION_STATUS,
                            ApplicantValidationStatus.NOT_SUCCESS
                    )
                    .expectVariable(
                            StateConstants.APPLICANT_VALIDATION_MESSAGE,
                            "Can't retrieve applicant data"
                    )
                    .and()
                .build();

        testPlan.test();

        verify(dataServiceClient, atLeastOnce()).findById(eq(applicantId));
        verify(applicationService, atLeastOnce()).createRecord(
                eq(applicantId),
                contains("Empty application record created")
        );
        verify(applicationService, atLeastOnce()).createRecord(
                eq(applicantId),
                contains("Data not received from party data service")
        );
        verify(applicationService, never()).createRecord(
                eq(applicantId),
                contains("Validation result is")
        );
    }

    @Test
    @DisplayName("Should stop when party data not valid")
    void execute_shouldStopWhenPartyDataNotValid() throws Exception {
        final UUID applicantId = UUID.randomUUID();
        final String validationErrorMessage = "Custom error message";
        final StateMachine<OrchestrationState, OrchestrationEvent> stateMachine = buildStateMachine(applicantId);

        when(dataServiceClient.findById(eq(applicantId))).thenReturn(Optional.of(
                ApplicantDTO.builder()
                        .applicantId(applicantId)
                        .build()
        ));
        when(partyDataValidator.validate(any(PartyDataAndAddress.class))).thenReturn(ApplicantValidationResult.of(
                ApplicantValidationStatus.NOT_SUCCESS,
                validationErrorMessage
        ));

        final StateMachineTestPlan<OrchestrationState, OrchestrationEvent> testPlan = StateMachineTestPlanBuilder.<OrchestrationState, OrchestrationEvent>builder()
                .stateMachine(stateMachine)
                .step()
                    .sendEvent(OrchestrationEvent.START_EXECUTION)
                    .expectState(OrchestrationState.APPLICANT_DATA_VALIDATION_FAILED_STATE)
                    .expectVariable(
                            StateConstants.APPLICANT_VALIDATION_STATUS,
                            ApplicantValidationStatus.NOT_SUCCESS
                    )
                    .expectVariable(
                            StateConstants.APPLICANT_VALIDATION_MESSAGE,
                            validationErrorMessage
                    )
                    .and()
                .build();

        testPlan.test();

        verify(partyDataValidator, atLeastOnce()).validate(any(PartyDataAndAddress.class));
        verify(applicationService, atLeastOnce()).createRecord(
                eq(applicantId),
                contains("Validation result is")
        );
    }

    @Test
    @DisplayName("Should stop when customer found in BDP")
    void execute_shouldStopWhenCustomerFoundInBDP() throws Exception {
        final UUID applicantId = UUID.randomUUID();
        final String fNumber = "F123456";
        final UUID bdpUuid = UUID.randomUUID();
        final StateMachine<OrchestrationState, OrchestrationEvent> stateMachine = buildStateMachine(applicantId);

        when(dataServiceClient.findById(eq(applicantId))).thenReturn(Optional.of(
                ApplicantDTO.builder()
                        .applicantId(applicantId)
                        .build()
        ));
        when(partyDataValidator.validate(any(PartyDataAndAddress.class))).thenReturn(ApplicantValidationResult.success());
        when(customerSearchClient.search(any(CustomerSearchRequest.class))).thenReturn(CustomerSearchResponse.builder()
                .status(CustomerSearchStatus.FOUND_SINGLE)
                .fnumber(fNumber)
                .bdpUuid(bdpUuid)
                .build());

        final StateMachineTestPlan<OrchestrationState, OrchestrationEvent> testPlan = StateMachineTestPlanBuilder.<OrchestrationState, OrchestrationEvent>builder()
                .stateMachine(stateMachine)
                .step()
                    .sendEvent(OrchestrationEvent.START_EXECUTION)
                    .expectState(OrchestrationState.CUSTOMER_FOUND_IN_BDP_STATE)
                    .expectVariable(
                            StateConstants.CORE_SEARCH_STATUS,
                            CustomerSearchStatus.FOUND_SINGLE
                    )
                    .expectVariable(
                            StateConstants.CORE_SEARCH_BDP_UUID,
                            bdpUuid
                    )
                    .expectVariable(
                            StateConstants.CORE_SEARCH_F_NUMBER,
                            fNumber
                    )
                    .and()
                .build();

        testPlan.test();

        final ArgumentCaptor<CustomerSearchRequest> requestCaptor = ArgumentCaptor.forClass(CustomerSearchRequest.class);
        verify(customerSearchClient, times(1)).search(requestCaptor.capture());

        assertThat(requestCaptor.getValue())
                .isNotNull()
                .extracting(CustomerSearchRequest::getApplicantId)
                .isEqualTo(applicantId);

        verify(applicationService, times(1)).createRecord(
                eq(applicantId),
                contains("Existing customer found")
        );
    }

    @Test
    @DisplayName("Should create customer when not found")
    void execute_shouldCreateCustomerWhenNotFound() throws Exception {
        final UUID applicantId = UUID.randomUUID();
        final String fNumber = "F123456";
        final UUID bdpUuid = UUID.randomUUID();
        final StateMachine<OrchestrationState, OrchestrationEvent> stateMachine = buildStateMachine(applicantId);

        when(dataServiceClient.findById(eq(applicantId))).thenReturn(Optional.of(
                ApplicantDTO.builder()
                        .applicantId(applicantId)
                        .build()
        ));
        when(partyDataValidator.validate(any(PartyDataAndAddress.class))).thenReturn(ApplicantValidationResult.success());
        when(customerSearchClient.search(any(CustomerSearchRequest.class))).thenReturn(CustomerSearchResponse.notFound());
        when(customerCreateClient.create(any(CustomerCreateRequest.class))).thenReturn(CustomerCreateResponse.builder()
                .fnumber(fNumber)
                .bdpUuid(bdpUuid)
                .build());

        final StateMachineTestPlan<OrchestrationState, OrchestrationEvent> testPlan = StateMachineTestPlanBuilder.<OrchestrationState, OrchestrationEvent>builder()
                .stateMachine(stateMachine)
                .step()
                    .sendEvent(OrchestrationEvent.START_EXECUTION)
                    .expectState(OrchestrationState.CUSTOMER_CREATION_STATE)
                    .expectVariable(
                            StateConstants.CORE_CREATE_F_NUMBER,
                            fNumber
                    )
                    .expectVariable(
                            StateConstants.CORE_CREATE_DBP_UUID,
                            bdpUuid
                    )
                    .and()
                .build();

        testPlan.test();
    }
}