package uk.co.santander.onboarding.services.orchestration.client.core;

import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.core.client.CustomerSearchRequest;
import uk.co.santander.onboarding.services.orchestration.model.PartyDataAndAddress;

/**
 * This component converts party data and address to the request for core API.
 */
@Component
public class CustomerSearchRequestAdapter {
    public CustomerSearchRequest build(final PartyDataAndAddress dataAndAddress) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
