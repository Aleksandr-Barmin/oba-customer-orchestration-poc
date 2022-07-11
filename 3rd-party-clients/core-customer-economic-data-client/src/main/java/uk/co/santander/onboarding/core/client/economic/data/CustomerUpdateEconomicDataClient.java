package uk.co.santander.onboarding.core.client.economic.data;

/**
 * Client for communication with Customer Update Economic Data Core API.
 */
public interface CustomerUpdateEconomicDataClient {
    /**
     * Update economic data for the given customer in BDP.
     *
     * @param request to be sent to Core API.
     * @return response from Core API.
     */
    CustomerEconomicDataUpdateResponse update(CustomerEconomicDataUpdateRequest request);
}
