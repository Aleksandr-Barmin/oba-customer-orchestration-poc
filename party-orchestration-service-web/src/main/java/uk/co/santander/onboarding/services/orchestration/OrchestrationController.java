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

/** As for now, the main controller of the application, kicks-off the on-boarding process. */
@RestController
public class OrchestrationController {
  @Autowired private OrchestrationService orchestrationService;

  /**
   * Kick-off the on-boarding process for the applicant described in the request.
   *
   * @param request to process.
   * @return as for now, this data is meaningless, need to update.
   */
  @PostMapping("/execute")
  public ResponseEntity<Object> execute(@RequestBody @Valid ApplicationStartRequest request) {
    final OrchestrationState currentState = orchestrationService.execute(request);
    if (currentState == OrchestrationState.APPLICANT_DATA_VALIDATION_FAILED_STATE) {
      return ResponseEntity.internalServerError().body("Can't get applicant data");
    }
    return ResponseEntity.ok(currentState);
  }
}
