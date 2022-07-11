package uk.co.santander.onboarding.services.orchestration;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.co.santander.onboarding.services.orchestration.model.ApplicationStartRequest;
import uk.co.santander.onboarding.services.orchestration.service.OrchestrationService;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationState;

/**
 * As for now, the main controller of the application, kicks-off the on-boarding process.
 */
@RestController
public class OrchestrationController {
    @Autowired
    private OrchestrationService orchestrationService;

    @PostMapping("/authorize")
    public ResponseEntity<Object> authorize(final @RequestBody @Valid ApplicationStartRequest request) {
        final OrchestrationState currentState = orchestrationService.authorize(request);
        if (OrchestrationState.APPLICANT_DATA_VALIDATION_FAILED_STATE == currentState) {
            return ResponseEntity.badRequest()
                    .body("Applicant data validation failed");
        }
        if (OrchestrationState.CUSTOMER_FOUND_IN_BDP_STATE == currentState) {
            return ResponseEntity.badRequest()
                    .body("Applicant found in BDP");
        }
        return ResponseEntity.ok(currentState);
    }

    /**
     * Kick-off the on-boarding process for the applicant described in the request.
     *
     * @param request to process.
     * @return as for now, this data is meaningless, need to update.
     */
    @PostMapping("/execute")
    public ResponseEntity<Object> execute(@RequestBody @Valid ApplicationStartRequest request) {
        final OrchestrationState currentState = orchestrationService.execute(request);
        return ResponseEntity.ok(currentState);
    }
}
