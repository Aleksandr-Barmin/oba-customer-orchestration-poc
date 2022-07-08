package uk.co.santander.onboarding.services.orchestration.client.core.simulator;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uk.co.santander.onboarding.core.client.create.CustomerCreateRequest;
import uk.co.santander.onboarding.core.client.create.CustomerCreateResponse;

/**
 * Feign client to connect to the Customer Create Core API service emulated by outer world
 * simulator.
 */
@FeignClient(name = "core-customer-create-client", url = "${client.core-customer-create.base-url}")
public interface CustomerCreateFeignClient {
  /**
   * Send a request to create a customer in BDP via Core API.
   *
   * @param request to be sent.
   * @return response from Core API (simulated response).
   */
  @PostMapping("/")
  CustomerCreateResponse createCustomer(@RequestBody CustomerCreateRequest request);
}
