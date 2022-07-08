package uk.co.santander.onboarding.services.orchestration.state.action;

import com.google.common.collect.Maps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.co.santander.onboarding.core.client.search.CustomerSearchClient;
import uk.co.santander.onboarding.core.client.search.CustomerSearchRequest;
import uk.co.santander.onboarding.core.client.search.CustomerSearchResponse;
import uk.co.santander.onboarding.core.client.search.CustomerSearchStatus;
import uk.co.santander.onboarding.services.orchestration.client.baas.PartyDataFacade;
import uk.co.santander.onboarding.services.orchestration.client.core.CustomerSearchRequestAdapter;
import uk.co.santander.onboarding.services.orchestration.model.ApplicantValidationStatus;
import uk.co.santander.onboarding.services.orchestration.model.PartyDataAndAddress;
import uk.co.santander.onboarding.services.orchestration.service.ApplicationService;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateConstants;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateContextHelper;
import uk.co.santander.onboarding.services.party.dto.ApplicantDTO;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static reactor.core.publisher.Mono.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        StateContextHelper.class,
        SearchCustomerInBdpAction.class
})
class SearchCustomerInBdpActionTest {
    @Autowired
    private SearchCustomerInBdpAction uut;

    @Autowired
    private StateContextHelper contextHelper;

    @MockBean
    private CustomerSearchClient customerSearchClient;

    @MockBean
    private CustomerSearchRequestAdapter requestAdapter;

    @MockBean
    private PartyDataFacade dataFacade;

    @MockBean
    private ApplicationService applicationService;

    @Mock
    private StateContext context;

    @Mock
    private ExtendedState extendedState;

    @Captor
    private ArgumentCaptor<CustomerSearchRequest> requestCaptor;

    @Test
    @DisplayName("Context should start")
    void check_contextStarts() {
        assertThat(uut).isNotNull();
    }

    @Test
    @DisplayName("Applicant ID should be in context")
    void execute_shouldHaveApplicantIdInContext() {
        when(context.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(Map.of());

        assertThatThrownBy(() -> {
            uut.execute(context);
        })
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Application ID should be provided");
    }

    @ParameterizedTest
    @EnumSource(
            value = ApplicantValidationStatus.class,
            mode = EnumSource.Mode.EXCLUDE,
            names = "SUCCESS"
    )
    @DisplayName("Validation status should be positive")
    void execute_validationStatusShouldBePositive(final ApplicantValidationStatus validationStatus) {
        when(context.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(Map.of(
                StateConstants.APPLICATION_ID, UUID.randomUUID(),
                StateConstants.APPLICANT_VALIDATION_STATUS, validationStatus
        ));

        assertThatThrownBy(() -> {
            uut.execute(context);
        })
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Validation result should be positive");
    }

    @Test
    @DisplayName("Should make requests to search Core API")
    void execute_shouldMakeRequestsToSearchCoreApi() {
        final UUID applicantId = UUID.randomUUID();

        when(context.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(Maps.newHashMap(Map.of(
                StateConstants.APPLICATION_ID, applicantId,
                StateConstants.APPLICANT_VALIDATION_STATUS, ApplicantValidationStatus.SUCCESS
        )));
        when(dataFacade.getPartyData(eq(applicantId))).thenReturn(PartyDataAndAddress.builder()
                .applicantOptional(Optional.of(
                        ApplicantDTO.builder()
                                .applicantId(applicantId)
                                .build()
                ))
                .build());
        when(requestAdapter.build(any(PartyDataAndAddress.class))).thenAnswer(inv -> {
            final PartyDataAndAddress request = inv.getArgument(0);
            return CustomerSearchRequest.builder()
                    .applicantId(request.getApplicantOptional().get().getApplicantId())
                    .build();
        });
        when(customerSearchClient.search(any(CustomerSearchRequest.class))).thenReturn(CustomerSearchResponse.builder()
                .status(CustomerSearchStatus.NOT_FOUND)
                .build());

        uut.execute(context);

        verify(customerSearchClient, times(1)).search(requestCaptor.capture());

        final CustomerSearchRequest searchRequest = requestCaptor.getValue();

        assertThat(searchRequest)
                .isNotNull()
                .withFailMessage("Request to Core API is null");

        assertThat(searchRequest.getApplicantId())
                .isEqualTo(applicantId)
                .withFailMessage("Invalid applicant ID provided");
    }

    @ParameterizedTest
    @EnumSource(CustomerSearchStatus.class)
    @DisplayName("Should save results of checks to the context")
    void execute_shouldSaveResultsToContext(final CustomerSearchStatus searchStatus) {
        final UUID applicantId = UUID.randomUUID();

        when(context.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(Maps.newHashMap(Map.of(
                StateConstants.APPLICATION_ID, applicantId,
                StateConstants.APPLICANT_VALIDATION_STATUS, ApplicantValidationStatus.SUCCESS
        )));
        when(dataFacade.getPartyData(eq(applicantId))).thenReturn(PartyDataAndAddress.builder()
                .applicantOptional(Optional.of(
                        ApplicantDTO.builder()
                                .applicantId(applicantId)
                                .build()
                ))
                .build());
        when(requestAdapter.build(any(PartyDataAndAddress.class))).thenAnswer(inv -> {
            final PartyDataAndAddress request = inv.getArgument(0);
            return CustomerSearchRequest.builder()
                    .applicantId(request.getApplicantOptional().get().getApplicantId())
                    .build();
        });
        when(customerSearchClient.search(any(CustomerSearchRequest.class))).thenReturn(CustomerSearchResponse.builder()
                .status(searchStatus)
                .build());

        uut.execute(context);

        final CustomerSearchStatus savedSearchStatus = contextHelper.getCustomerSearchStatus(context);

        assertThat(savedSearchStatus)
                .isEqualTo(searchStatus)
                .withFailMessage("Invalid status saved");

        verify(applicationService, times(1)).createRecord(
                eq(applicantId),
                contains("Searching for existing customer in core API")
        );

        if (!searchStatus.isFound()) {
            verify(applicationService, times(1)).createRecord(
                    eq(applicantId),
                    contains("Customer not found")
            );
        } else {
            verify(applicationService, times(1)).createRecord(
                    eq(applicantId),
                    contains("Existing customer found")
            );
        }
    }

    @Test
    @DisplayName("Should save F-number and BDP id to context when found")
    void execute_shouldSaveFNumberAndBDPIdToContext() {
        final UUID applicantId = UUID.randomUUID();
        final UUID bdpId = UUID.randomUUID();
        final String fNumber = "F123456";

        when(context.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(Maps.newHashMap(Map.of(
                StateConstants.APPLICATION_ID, applicantId,
                StateConstants.APPLICANT_VALIDATION_STATUS, ApplicantValidationStatus.SUCCESS
        )));
        when(dataFacade.getPartyData(eq(applicantId))).thenReturn(PartyDataAndAddress.builder()
                .applicantOptional(Optional.of(
                        ApplicantDTO.builder()
                                .applicantId(applicantId)
                                .build()
                ))
                .build());
        when(requestAdapter.build(any(PartyDataAndAddress.class))).thenAnswer(inv -> {
            final PartyDataAndAddress request = inv.getArgument(0);
            return CustomerSearchRequest.builder()
                    .applicantId(request.getApplicantOptional().get().getApplicantId())
                    .build();
        });
        when(customerSearchClient.search(any(CustomerSearchRequest.class))).thenReturn(CustomerSearchResponse.builder()
                .status(CustomerSearchStatus.FOUND_SINGLE)
                .bdpUuid(bdpId)
                .fnumber(fNumber)
                .build());

        uut.execute(context);

        assertThat(context.getExtendedState().getVariables())
                .extractingByKeys(
                        StateConstants.CORE_SEARCH_STATUS,
                        StateConstants.CORE_SEARCH_F_NUMBER,
                        StateConstants.CORE_SEARCH_BDP_UUID
                )
                .containsExactly(
                        CustomerSearchStatus.FOUND_SINGLE,
                        fNumber,
                        bdpId
                );
    }
}