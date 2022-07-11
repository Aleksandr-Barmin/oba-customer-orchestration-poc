package uk.co.santander.onboarding.services.orchestration.state.action;

import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.services.orchestration.client.baas.PartyDataFacade;
import uk.co.santander.onboarding.services.orchestration.model.ApplicantValidationResult;
import uk.co.santander.onboarding.services.orchestration.model.PartyDataAndAddress;
import uk.co.santander.onboarding.services.orchestration.service.ApplicationService;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationEvent;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationState;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateContextHelper;
import uk.co.santander.onboarding.services.orchestration.state.validator.PartyDataAndAddressValidator;

/**
 * This action retrieves applicant's data from BaaS API and validates it.
 */
@Component
public class GetAndVerifyApplicantDataAction
        implements Action<OrchestrationState, OrchestrationEvent> {
    @Autowired
    private StateContextHelper helper;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private PartyDataFacade partyDataFacade;

    @Autowired
    private PartyDataAndAddressValidator dataValidator;

    @Override
    public void execute(StateContext<OrchestrationState, OrchestrationEvent> context) {
        final UUID applicationId = helper.getApplicationId(context);
        Objects.requireNonNull(applicationId, "Application ID should be provided");

        applicationService.createRecord(applicationId, "Start getting info from party data service");

        final PartyDataAndAddress partyData = partyDataFacade.getPartyData(applicationId);

        // can't proceed without applicant
        if (partyData.getApplicantOptional().isEmpty()) {
            helper.setApplicantValidationResult(context, ApplicantValidationResult.noApplicant());

            applicationService.createRecord(applicationId, "Data not received from party data service");
            return;
        }

        // validating
        final ApplicantValidationResult validationResult = dataValidator.validate(partyData);

        helper.setApplicantValidationResult(context, validationResult);

        applicationService.createRecord(
                applicationId, String.format("Validation result is %s", validationResult.getStatus()));
    }
}
