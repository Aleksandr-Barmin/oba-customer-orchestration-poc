package uk.co.santander.onboarding.services.orchestration.state.action;

import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.core.client.create.CustomerCreateClient;
import uk.co.santander.onboarding.core.client.create.CustomerCreateRequest;
import uk.co.santander.onboarding.core.client.create.CustomerCreateResponse;
import uk.co.santander.onboarding.core.client.search.CustomerSearchStatus;
import uk.co.santander.onboarding.services.orchestration.client.baas.PartyDataFacade;
import uk.co.santander.onboarding.services.orchestration.client.core.CustomerCreateRequestAdapter;
import uk.co.santander.onboarding.services.orchestration.model.PartyDataAndAddress;
import uk.co.santander.onboarding.services.orchestration.service.ApplicationService;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationEvent;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationState;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateConstants;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateContextHelper;

/**
 * This action gets an applicant data from BaaS systems (or from cache if data was requested
 * earlier) and sends a request to Core API to create a new customer.
 */
@Component
public class CreateCustomerInBdpAction implements Action<OrchestrationState, OrchestrationEvent> {
  @Autowired private StateContextHelper helper;

  @Autowired private CustomerCreateRequestAdapter requestAdapter;

  @Autowired private CustomerCreateClient createClient;

  @Autowired private PartyDataFacade partyDataFacade;

  @Autowired private ApplicationService applicationService;

  @Override
  public void execute(StateContext<OrchestrationState, OrchestrationEvent> context) {
    final UUID applicationId = helper.getApplicationId(context);
    if (Objects.isNull(applicationId)) {
      throw new IllegalStateException("Applicant ID should be in context");
    }

    final CustomerSearchStatus searchStatus = helper.getCustomerSearchStatus(context);
    if (Objects.isNull(searchStatus)) {
      throw new IllegalStateException("Search results should be in context");
    }

    if (searchStatus.isFound()) {
      throw new IllegalStateException("Search result should be negative");
    }

    applicationService.createRecord(applicationId, "Creating customer in BDP");

    final PartyDataAndAddress partyData = partyDataFacade.getPartyData(applicationId);
    final CustomerCreateRequest createCustomerRequest = requestAdapter.build(partyData);

    final CustomerCreateResponse response = createClient.create(createCustomerRequest);

    // TODO: rewrite using helper
    context
        .getExtendedState()
        .getVariables()
        .put(StateConstants.CORE_CREATE_DBP_UUID, response.getBdpUuid());
    context
        .getExtendedState()
        .getVariables()
        .put(StateConstants.CORE_CREATE_F_NUMBER, response.getFnumber());

    applicationService.createRecord(applicationId, "Customer created in BDP");
  }
}
