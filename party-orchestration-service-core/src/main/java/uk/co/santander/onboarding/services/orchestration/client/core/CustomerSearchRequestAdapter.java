package uk.co.santander.onboarding.services.orchestration.client.core;

import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.core.client.search.CustomerSearchRequest;
import uk.co.santander.onboarding.services.orchestration.model.PartyDataAndAddress;

/**
 * This component converts party data and address to the request for core API.
 */
@Component
public class CustomerSearchRequestAdapter {
    @DummyImplementation
    public CustomerSearchRequest build(final PartyDataAndAddress dataAndAddress) {
        return CustomerSearchRequest.builder()
                .applicantId(dataAndAddress.getApplicantOptional().get()
                        .getApplicantId())
                .build();
    }
}
