package uk.co.santander.onboarding.services.orchestration.client.core;

import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.core.client.create.CustomerCreateRequest;
import uk.co.santander.onboarding.services.orchestration.model.PartyDataAndAddress;

/**
 * This component converts party data and address to the request for core API.
 */
@Component
@DummyImplementation
public class CustomerCreateRequestAdapter {
    /**
     * Build a request for sending to the Customer Create Core API.
     *
     * @param partyData information about applicant.
     * @return request for Core API.
     */
    public CustomerCreateRequest build(final PartyDataAndAddress partyData) {
        return CustomerCreateRequest.builder()
                .applicantId(partyData.getApplicantOptional().get().getApplicantId())
                .build();
    }
}
