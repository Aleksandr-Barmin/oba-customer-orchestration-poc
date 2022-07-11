package uk.co.santander.onboarding.services.orchestration.state.action;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.co.santander.onboarding.core.client.economic.data.CustomerEconomicDataUpdateRequest;
import uk.co.santander.onboarding.core.client.economic.data.CustomerUpdateEconomicDataClient;
import uk.co.santander.onboarding.services.orchestration.client.baas.PartyDataFacade;
import uk.co.santander.onboarding.services.orchestration.client.core.CustomerEconomicActivityUpdateRequestAdapter;
import uk.co.santander.onboarding.services.orchestration.model.PartyDataAndAddress;
import uk.co.santander.onboarding.services.orchestration.service.ApplicationService;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateConstants;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateContextHelper;
import uk.co.santander.onboarding.services.party.dto.ApplicantDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.contains;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                StateContextHelper.class,
                AddEconomicDataAction.class,
                CustomerEconomicActivityUpdateRequestAdapter.class
        })
class AddEconomicDataActionTest {
    @Autowired
    private AddEconomicDataAction uut;

    @MockBean
    private PartyDataFacade dataFacade;

    @MockBean
    private CustomerUpdateEconomicDataClient economicDataClient;

    @MockBean
    private ApplicationService applicationService;

    @Mock
    private StateContext context;

    @Mock
    private ExtendedState extendedState;

    @Test
    @DisplayName("Context should start")
    void context_shouldStart() {
        assertThat(uut).isNotNull().withFailMessage("Context should start");
    }

    @Test
    @DisplayName("If BDP is not in context, an exception should be thrown")
    void execute_BDPUuidShouldBeInContext() {
        when(context.getExtendedState()).thenReturn(extendedState);
        when(extendedState.get(any(), any(Class.class))).thenReturn(null);

        assertThatThrownBy(
                () -> {
                    uut.execute(context);
                })
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("BDP UUID should be in context");
    }

    @Test
    @DisplayName("If F-Number is not in context, an exception should be thrown")
    void execute_FNumberShouldBeInContext() {
        final Map<String, Object> contextState =
                Map.of(StateConstants.CORE_CREATE_DBP_UUID, UUID.randomUUID());

        when(context.getExtendedState()).thenReturn(extendedState);
        when(extendedState.get(any(), any(Class.class)))
                .thenAnswer(
                        inv -> {
                            final String key = inv.getArgument(0, String.class);
                            return contextState.get(key);
                        });

        assertThatThrownBy(
                () -> {
                    uut.execute(context);
                })
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("F-Number should be in context");
    }

    @Test
    @DisplayName("Data should be requested from BaaS and sent to Core API")
    void execute_shouldRetrieveDataFromBaas() {
        final UUID applicantId = UUID.randomUUID();
        final UUID bdpUuid = UUID.randomUUID();

        final Map<Object, Object> contextState =
                Map.of(
                        StateConstants.CORE_CREATE_DBP_UUID,
                        bdpUuid,
                        StateConstants.CORE_CREATE_F_NUMBER,
                        "F123456",
                        StateConstants.APPLICATION_ID,
                        applicantId);

        when(context.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(contextState);
        when(extendedState.get(any(), any(Class.class)))
                .thenAnswer(
                        inv -> {
                            final String key = inv.getArgument(0, String.class);
                            return contextState.get(key);
                        });
        when(dataFacade.getPartyData(eq(applicantId)))
                .thenReturn(
                        PartyDataAndAddress.builder()
                                .addresses(List.of())
                                .applicantOptional(Optional.of(ApplicantDTO.builder().build()))
                                .build());

        uut.execute(context);

        final ArgumentCaptor<CustomerEconomicDataUpdateRequest> requestCaptor =
                ArgumentCaptor.forClass(CustomerEconomicDataUpdateRequest.class);

        verify(dataFacade, atLeastOnce()).getPartyData(eq(applicantId));
        verify(economicDataClient, atLeastOnce()).update(requestCaptor.capture());

        assertThat(requestCaptor.getValue())
                .extracting(CustomerEconomicDataUpdateRequest::getBdpUuid)
                .isEqualTo(bdpUuid)
                .withFailMessage("BDP UUID was not sent to Core API");

        verify(applicationService, atLeastOnce()).createRecord(
                eq(applicantId),
                contains("Economic data is updated in BDP"),
                any()
        );
    }
}
