package uk.co.santander.onboarding.core.client.create;

public interface CustomerCreateClient {
    /**
     * Create a customer in BDP via Core API.
     *
     * @param request
     * @return
     */
    CustomerCreateResponse create(CustomerCreateRequest request);
}
