package uk.co.santander.onboarding.services.orchestration.client.core.simulator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.santander.onboarding.core.client.search.CustomerSearchClient;
import uk.co.santander.onboarding.core.client.search.CustomerSearchRequest;
import uk.co.santander.onboarding.core.client.search.CustomerSearchResponse;
import uk.co.santander.onboarding.services.orchestration.client.core.DummyImplementation;

@Service
@DummyImplementation
public class CustomerSearchSimulatorClient implements CustomerSearchClient {
  @Autowired private CustomerSearchFeignClient feignClient;

  @Override
  public CustomerSearchResponse search(CustomerSearchRequest request) {
    return feignClient.searchCustomer(request);
  }
}
