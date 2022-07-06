package uk.co.santander.onboarding.services.orchestration.state.action;

import com.google.common.collect.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.services.address.address.dto.AddressDTO;
import uk.co.santander.onboarding.services.orchestration.client.PartyAddressServiceClient;
import uk.co.santander.onboarding.services.orchestration.client.PartyDataServiceClient;
import uk.co.santander.onboarding.services.orchestration.model.ApplicantValidationResult;
import uk.co.santander.onboarding.services.orchestration.service.ApplicationService;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationEvent;
import uk.co.santander.onboarding.services.orchestration.state.OrchestrationState;
import uk.co.santander.onboarding.services.orchestration.state.helper.StateContextHelper;
import uk.co.santander.onboarding.services.orchestration.state.validator.PartyDataAndAddress;
import uk.co.santander.onboarding.services.orchestration.state.validator.PartyDataAndAddressValidator;
import uk.co.santander.onboarding.services.party.dto.ApplicantDTO;
import uk.co.santander.onboarding.services.party.dto.ContactPointDTO;
import uk.co.santander.onboarding.services.party.dto.PostalAddressesDTO;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class GetAndVerifyApplicantDataAction implements Action<OrchestrationState, OrchestrationEvent> {
    @Autowired
    private StateContextHelper helper;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private PartyDataServiceClient dataServiceClient;

    @Autowired
    private PartyAddressServiceClient addressServiceClient;

    @Autowired
    private PartyDataAndAddressValidator dataValidator;

    @Override
    public void execute(StateContext<OrchestrationState, OrchestrationEvent> context) {
        final UUID applicationId = helper.getApplicationId(context);
        Objects.requireNonNull(applicationId, "Application ID should be provided");

        applicationService.createRecord(applicationId, "Start getting info from party data service");

        final Optional<ApplicantDTO> applicantOptional = dataServiceClient.findById(applicationId);

        final Optional<AddressDTO> addressOptional = applicantOptional.map(ApplicantDTO::getContactPoint)
                .map(ContactPointDTO::getPostalAddresses)
                .map(Iterables::getLast) // TODO not sure if only one address is available
                .map(PostalAddressesDTO::getAddressId)
                .flatMap(addressServiceClient::findById);

        // can't proceed without applicant
        if (applicantOptional.isEmpty()) {
            helper.setApplicantValidationResult(context, ApplicantValidationResult.noApplicant());

            applicationService.createRecord(applicationId, "Data not received from party data service");
            return;
        }

        // validating
        final ApplicantValidationResult validationInfo = dataValidator.validate(new PartyDataAndAddress(
                applicantOptional,
                addressOptional
        ));

        helper.setApplicantValidationResult(context, validationInfo);

        applicationService.createRecord(
                applicationId,
                String.format(
                        "Validation result is %s",
                        validationInfo.getStatus()
                )
        );
    }
}
