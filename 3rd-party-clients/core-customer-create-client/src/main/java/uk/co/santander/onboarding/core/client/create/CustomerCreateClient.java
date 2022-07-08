package uk.co.santander.onboarding.core.client.create;

/**
 * Client for communication with Customer Search Core API.
 */
public interface CustomerCreateClient {
    /**
     * Create a customer in BDP via Core API.
     *
     * @param request is data to be sent to Core API.
     * @return response received from Core API.
     */
    CustomerCreateResponse create(CustomerCreateRequest request);
}
