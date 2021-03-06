package uk.co.santander.onboarding.services.orchestration.client.core.simulator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.santander.onboarding.core.client.create.CustomerCreateClient;
import uk.co.santander.onboarding.core.client.create.CustomerCreateRequest;
import uk.co.santander.onboarding.core.client.create.CustomerCreateResponse;
import uk.co.santander.onboarding.services.orchestration.client.core.DummyImplementation;

/**
 * Dummy implementation of the {@link CustomerCreateClient} which uses feign client for sending
 * requests to the outer world simulator.
 */
@Service
@DummyImplementation
public class CustomerCreateSimulatorClient implements CustomerCreateClient {
    @Autowired
    private CustomerCreateFeignClient feignClient;

    @Override
    public CustomerCreateResponse create(CustomerCreateRequest request) {
        return feignClient.createCustomer(request);
    }
}
