package uk.co.santander.onboarding.services.orchestration.state.action;

import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.core.client.search.CustomerSearchClient;
import uk.co.santander.onboarding.core.client.search.CustomerSearchRequest;
import uk.co.santander.onboarding.core.client.search.CustomerSearchResponse;
import uk.co.santander.onboarding.services.orchestration.client.baas.PartyDataFacade;
import uk.co.santander.onboarding.services.orchestration.client.core.CustomerSearchRequestAdapter;
import uk.co.santander.onboarding.services.orchestration.model.ApplicantValidationResult;
import uk.co.santander.onboarding.services.orchestration.model.PartyDataAndAddress;
import uk.co.santander.onboarding.services.orchestration.service.ApplicationService;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationEvent;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationState;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateConstants;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateContextHelper;

@Component
public class SearchCustomerInBdpAction implements Action<OrchestrationState, OrchestrationEvent> {
  @Autowired private StateContextHelper helper;

  @Autowired private CustomerSearchClient customerSearchClient;

  @Autowired private CustomerSearchRequestAdapter requestAdapter;

  @Autowired private PartyDataFacade partyDataFacade;

  @Autowired private ApplicationService applicationService;

  @Override
  public void execute(StateContext<OrchestrationState, OrchestrationEvent> context) {
    final UUID applicationId = helper.getApplicationId(context);
    Objects.requireNonNull(applicationId, "Application ID should be provided");

    final ApplicantValidationResult validationResult =
        helper.getApplicationValidationResult(context);
    if (!validationResult.isPositive()) {
      throw new IllegalStateException("Validation result should be positive");
    }

    applicationService.createRecord(applicationId, "Searching for existing customer in core API");

    final PartyDataAndAddress partyData = partyDataFacade.getPartyData(applicationId);
    final CustomerSearchRequest coreSearchRequest = requestAdapter.build(partyData);

    final CustomerSearchResponse coreSearchResponse =
        customerSearchClient.search(coreSearchRequest);

    helper.setCustomerSearchStatus(context, coreSearchResponse.getStatus());
    if (coreSearchResponse.getStatus().isFound()) {
      // TODO: rewrite using helper
      context
          .getExtendedState()
          .getVariables()
          .put(StateConstants.CORE_SEARCH_BDP_UUID, coreSearchResponse.getBdpUuid());
      context
          .getExtendedState()
          .getVariables()
          .put(StateConstants.CORE_SEARCH_F_NUMBER, coreSearchResponse.getFnumber());

      applicationService.createRecord(applicationId, "Existing customer found");
    } else {
      applicationService.createRecord(applicationId, "Customer not found");
    }
  }
}
