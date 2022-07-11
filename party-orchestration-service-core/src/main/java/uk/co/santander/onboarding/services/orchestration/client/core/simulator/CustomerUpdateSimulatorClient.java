package uk.co.santander.onboarding.services.orchestration.client.core.simulator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.santander.onboarding.core.client.economic.data.CustomerEconomicDataUpdateRequest;
import uk.co.santander.onboarding.core.client.economic.data.CustomerEconomicDataUpdateResponse;
import uk.co.santander.onboarding.core.client.economic.data.CustomerUpdateEconomicDataClient;
import uk.co.santander.onboarding.services.orchestration.client.core.DummyImplementation;

@Service
@DummyImplementation
public class CustomerUpdateSimulatorClient implements CustomerUpdateEconomicDataClient {
    @Autowired
    private CustomerUpdateFeignClient feignClient;

    @Override
    public CustomerEconomicDataUpdateResponse update(CustomerEconomicDataUpdateRequest request) {
        return feignClient.update(
                request.getBdpUuid(),
                request
        );
    }
}
