package uk.co.santander.onboarding.services.orchestration.client.core.simulator;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uk.co.santander.onboarding.core.client.economic.data.CustomerEconomicDataUpdateRequest;
import uk.co.santander.onboarding.core.client.economic.data.CustomerEconomicDataUpdateResponse;

@FeignClient(name = "core-customer-update-client", url = "${client.core-customer-update.base-url}")
public interface CustomerUpdateFeignClient {
    @PostMapping("/{bdpUuid}")
    CustomerEconomicDataUpdateResponse update(
            @PathVariable("bdpUuid") UUID bdpUuid,
            @RequestBody CustomerEconomicDataUpdateRequest request
    );
}
