package uk.co.santander.onboarding.services.orchestration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.co.santander.onboarding.services.orchestration.model.ApplicationStartRequest;
import uk.co.santander.onboarding.services.orchestration.service.OrchestrationService;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationState;

import javax.validation.Valid;

@RestController
public class OrchestrationController {
    @Autowired
    private OrchestrationService orchestrationService;

    @PostMapping("/execute")
    public ResponseEntity<Object> execute(@RequestBody @Valid ApplicationStartRequest request) {
        final OrchestrationState currentState = orchestrationService.execute(request);
        if (currentState == OrchestrationState.APPLICANT_DATA_VALIDATION_FAILED_STATE) {
            return ResponseEntity.internalServerError()
                    .body("Can't get applicant data");
        }
        return ResponseEntity.ok(currentState);
    }
}
