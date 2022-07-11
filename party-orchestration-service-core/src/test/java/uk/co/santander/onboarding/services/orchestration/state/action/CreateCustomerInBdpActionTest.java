package uk.co.santander.onboarding.services.orchestration.state.action;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.UUID;
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
import uk.co.santander.onboarding.core.client.create.CustomerCreateClient;
import uk.co.santander.onboarding.core.client.create.CustomerCreateRequest;
import uk.co.santander.onboarding.core.client.create.CustomerCreateResponse;
import uk.co.santander.onboarding.core.client.search.CustomerSearchStatus;
import uk.co.santander.onboarding.services.orchestration.client.baas.PartyDataFacade;
import uk.co.santander.onboarding.services.orchestration.client.core.CustomerCreateRequestAdapter;
import uk.co.santander.onboarding.services.orchestration.model.PartyDataAndAddress;
import uk.co.santander.onboarding.services.orchestration.service.ApplicationService;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateConstants;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateContextHelper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.contains;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        StateContextHelper.class,
        CreateCustomerInBdpAction.class
})
class CreateCustomerInBdpActionTest {
    @Autowired
    private CreateCustomerInBdpAction uut;

    @MockBean
    private CustomerCreateClient createClient;

    @MockBean
    private PartyDataFacade partyDataFacade;

    @MockBean
    private CustomerCreateRequestAdapter requestAdapter;

    @MockBean
    private ApplicationService applicationService;

    @Mock
    private StateContext context;

    @Mock
    private ExtendedState extendedState;

    @Test
    @DisplayName("Applicant id should be in context")
    void execute_applicantIdShouldBeInContext() {
        when(context.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(Map.of());

        assertThatThrownBy(() -> {
            uut.execute(context);
        })
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Applicant ID should be in context");
    }

    @Test
    @DisplayName("Search results should be in context")
    void execute_searchResultsShouldBeInContext() {
        when(context.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(Map.of(
                StateConstants.APPLICATION_ID,
                UUID.randomUUID()
        ));

        assertThatThrownBy(() -> {
            uut.execute(context);
        })
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Search results should be in context");
    }

    @ParameterizedTest
    @EnumSource(
            value = CustomerSearchStatus.class,
            mode = EnumSource.Mode.EXCLUDE,
            names = "NOT_FOUND"
    )
    @DisplayName("Search results should be negative")
    void execute_searchResultsShouldBeNotFound(final CustomerSearchStatus savedStatus) {
        when(context.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(Map.of(
                StateConstants.APPLICATION_ID,
                UUID.randomUUID(),

                StateConstants.CORE_SEARCH_STATUS,
                savedStatus
        ));

        assertThatThrownBy(() -> {
            uut.execute(context);
        })
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Search result should be negative");
    }

    @Test
    @DisplayName("Should send request to Core API")
    void execute_shouldCallCoreApi() {
        final CustomerCreateRequest createRequest = CustomerCreateRequest.builder().build();
        final UUID applicantId = UUID.randomUUID();
        final UUID bdpUuid = UUID.randomUUID();
        final String fNumber = "F123456";

        when(context.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(Maps.newHashMap(Map.of(
                StateConstants.APPLICATION_ID,
                applicantId,

                StateConstants.CORE_SEARCH_STATUS,
                CustomerSearchStatus.NOT_FOUND
        )));
        when(partyDataFacade.getPartyData(eq(applicantId))).thenReturn(PartyDataAndAddress.builder().build());
        when(requestAdapter.build(any(PartyDataAndAddress.class))).thenReturn(createRequest);
        when(createClient.create(eq(createRequest))).thenReturn(CustomerCreateResponse.builder()
                .bdpUuid(bdpUuid)
                .fnumber(fNumber)
                .build());

        uut.execute(context);

        verify(createClient, atLeastOnce()).create(eq(createRequest));

        assertThat(context.getExtendedState().getVariables())
                .extractingByKeys(
                        StateConstants.CORE_CREATE_DBP_UUID,
                        StateConstants.CORE_CREATE_F_NUMBER
                )
                .containsExactly(
                        bdpUuid,
                        fNumber
                );

        verify(applicationService, times(1)).createRecord(
                eq(applicantId),
                contains("Creating customer in BDP")
        );

        verify(applicationService, times(1)).createRecord(
                eq(applicantId),
                contains("Customer created in BDP")
        );
    }
}