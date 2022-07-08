package uk.co.santander.onboarding.services.orchestration.client.core.simulator;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uk.co.santander.onboarding.core.client.search.CustomerSearchRequest;
import uk.co.santander.onboarding.core.client.search.CustomerSearchResponse;

/**
 * Feign client to connect to the Customer Search Core API service emulated by outer world
 * simulator.
 */
@FeignClient(name = "core-customer-search-client", url = "${client.core-customer-search.base-url}")
public interface CustomerSearchFeignClient {
  /**
   * Send a request to search for a customer in BDP via Core API.
   *
   * @param request to be sent to Core API.
   * @return response from Core API.
   */
  @PostMapping("")
  CustomerSearchResponse searchCustomer(@RequestBody CustomerSearchRequest request);
}
