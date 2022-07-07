package uk.co.santander.onboarding.core.client;

public interface CustomerSearchClient {
    /**
     * Try to find customer in BDP.
     * @param request
     * @return
     */
    CustomerSearchResponse search(CustomerSearchRequest request);
}
