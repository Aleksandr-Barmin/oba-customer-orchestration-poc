package uk.co.santander.onboarding.services.orchestration.state.guard;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.core.client.search.CustomerSearchStatus;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationEvent;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationState;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateContextHelper;

@Component
public class CustomerNotFoundInBDPGuard implements Guard<OrchestrationState, OrchestrationEvent> {
  @Autowired private StateContextHelper helper;

  @Override
  public boolean evaluate(StateContext<OrchestrationState, OrchestrationEvent> context) {
    return Optional.ofNullable(helper.getCustomerSearchStatus(context))
        .map(CustomerSearchStatus::isFound)
        .map(value -> !value)
        .orElse(Boolean.TRUE); // not allowing by default
  }
}
