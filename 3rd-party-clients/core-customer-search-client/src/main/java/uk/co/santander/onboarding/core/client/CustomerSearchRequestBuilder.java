package uk.co.santander.onboarding.core.client;

import org.springframework.stereotype.Component;

/**
 * This component converts party data and address to the request for core API.
 */
@Component
public class CustomerSearchRequestBuilder {
    public CustomerSearchRequest build(final PartyDataAndAddress dataAndAddress) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
