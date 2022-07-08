package uk.co.santander.onboarding.services.orchestration.state.action;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.services.orchestration.service.ApplicationService;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationEvent;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationState;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateContextHelper;

@Slf4j
@Component
public class OnMachineInitialization implements Action<OrchestrationState, OrchestrationEvent> {
  @Autowired private StateContextHelper contextHelper;

  @Autowired private ApplicationService applicationService;

  @Override
  public void execute(StateContext<OrchestrationState, OrchestrationEvent> context) {
    final UUID applicationId = contextHelper.getApplicationId(context);
    applicationService.createRecord(applicationId, "Empty application record created");
  }
}
