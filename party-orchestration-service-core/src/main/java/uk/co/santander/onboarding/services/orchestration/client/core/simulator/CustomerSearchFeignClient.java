package uk.co.santander.onboarding.services.orchestration.client.core.simulator;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uk.co.santander.onboarding.core.client.search.CustomerSearchRequest;
import uk.co.santander.onboarding.core.client.search.CustomerSearchResponse;

@FeignClient(name = "core-customer-search-client", url = "${client.core-customer-search.base-url}")
public interface CustomerSearchFeignClient {
    @PostMapping("")
    CustomerSearchResponse searchCustomer(@RequestBody CustomerSearchRequest request);
}
