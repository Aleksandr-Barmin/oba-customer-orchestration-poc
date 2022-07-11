package uk.co.santander.onboarding.services.orchestration.state.action;

import java.util.Objects;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.core.client.economic.data.CustomerEconomicDataUpdateRequest;
import uk.co.santander.onboarding.core.client.economic.data.CustomerEconomicDataUpdateResponse;
import uk.co.santander.onboarding.core.client.economic.data.CustomerUpdateEconomicDataClient;
import uk.co.santander.onboarding.services.orchestration.client.baas.PartyDataFacade;
import uk.co.santander.onboarding.services.orchestration.client.core.CustomerEconomicActivityUpdateRequestAdapter;
import uk.co.santander.onboarding.services.orchestration.model.PartyDataAndAddress;
import uk.co.santander.onboarding.services.orchestration.service.ApplicationService;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationEvent;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationState;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateConstants;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateContextHelper;

/**
 * This action adds economic data to the BDP record.
 */
@Component
public class AddEconomicDataAction implements Action<OrchestrationState, OrchestrationEvent> {
    @Autowired
    private StateContextHelper helper;

    @Autowired
    private PartyDataFacade dataFacade;

    @Autowired
    private CustomerEconomicActivityUpdateRequestAdapter requestAdapter;

    @Autowired
    private CustomerUpdateEconomicDataClient economicDataClient;

    @Autowired
    private ApplicationService applicationService;

    @Override
    public void execute(StateContext<OrchestrationState, OrchestrationEvent> context) {
        // TODO: do it via helper
        final UUID bpdUuid =
                context.getExtendedState().get(StateConstants.CORE_CREATE_DBP_UUID, UUID.class);

        if (Objects.isNull(bpdUuid)) {
            throw new IllegalStateException("BDP UUID should be in context");
        }

        final String fNumber =
                context.getExtendedState().get(StateConstants.CORE_CREATE_F_NUMBER, String.class);
        if (StringUtils.isEmpty(fNumber)) {
            throw new IllegalStateException("F-Number should be in context");
        }

        final UUID applicantId = helper.getApplicationId(context);
        if (Objects.isNull(applicantId)) {
            throw new IllegalStateException("Applicant ID should be in context");
        }

        final PartyDataAndAddress applicantData = dataFacade.getPartyData(applicantId);
        final CustomerEconomicDataUpdateRequest coreRequest =
                requestAdapter.create(bpdUuid, applicantData);
        final CustomerEconomicDataUpdateResponse updateResponse =
                economicDataClient.update(coreRequest);

        applicationService.createRecord(
                applicantId,
                "Economic data is updated in BDP",
                updateResponse
        );
    }
}
