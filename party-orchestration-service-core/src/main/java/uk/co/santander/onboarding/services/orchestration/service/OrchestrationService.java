package uk.co.santander.onboarding.services.orchestration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.santander.onboarding.services.orchestration.model.ApplicationStartRequest;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationEvent;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationState;

@Service
public class OrchestrationService {
    @Autowired
    private StateMachineService stateMachineService;

    public OrchestrationState execute(ApplicationStartRequest request) {
        // at this moment request already validated, need to kickstart the process

        // TODO: should we check if a process already started?
        return stateMachineService.sendEvent(request.getApplicantId(), OrchestrationEvent.START_EXECUTION);
    }
}
