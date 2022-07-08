package uk.co.santander.onboarding.services.orchestration.client.core.simulator;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uk.co.santander.onboarding.core.client.create.CustomerCreateRequest;
import uk.co.santander.onboarding.core.client.create.CustomerCreateResponse;

@FeignClient(name = "core-customer-create-client", url = "${client.core-customer-create.base-url}")
public interface CustomerCreateFeignClient {
  @PostMapping("/")
  CustomerCreateResponse createCustomer(@RequestBody CustomerCreateRequest request);
}
