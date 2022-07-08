package uk.co.santander.onboarding.services.orchestration.core;

import io.swagger.annotations.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.santander.onboarding.core.client.create.CustomerCreateRequest;
import uk.co.santander.onboarding.core.client.create.CustomerCreateResponse;
import uk.co.santander.onboarding.core.client.search.CustomerSearchRequest;
import uk.co.santander.onboarding.core.client.search.CustomerSearchResponse;
import uk.co.santander.onboarding.core.client.search.CustomerSearchStatus;
import uk.co.santander.onboarding.services.orchestration.config.WorldSimulatorConfig;

import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/core/customer")
@Tag(name = "Core API", description = "Core API Simulators")
public class CoreApiSimulationController {
    @Autowired
    private WorldSimulatorConfig config;

    @SneakyThrows
    @PostMapping("/search")
    @Operation(summary = "Search for a customer")
    @ApiResponse(code = 200, response = CustomerSearchResponse.class, message = "Result of customer search")
    public CustomerSearchResponse searchCustomer(final @RequestBody @Valid CustomerSearchRequest request) {
        final WorldSimulatorConfig.CoreCustomerSearch searchConfig = config.getCustomerSearch();
        TimeUnit.NANOSECONDS.sleep(searchConfig.getDelay().getNano());

        if (searchConfig.isFound()) {
            return CustomerSearchResponse.builder()
                    .status(CustomerSearchStatus.FOUND_SINGLE)
                    .bdpUuid(UUID.randomUUID())
                    .fnumber("F123456")
                    .build();
        }
        return CustomerSearchResponse.builder()
                .status(CustomerSearchStatus.NOT_FOUND)
                .build();
    }

    @SneakyThrows
    @PostMapping("/create")
    @Operation(summary = "Create a new customer")
    @ApiResponse(code = 200, response = CustomerCreateResponse.class, message = "Result of customer creation")
    public CustomerCreateResponse createCustomer(final @RequestBody @Valid CustomerCreateRequest request) {
        final WorldSimulatorConfig.CoreCustomerCreate createConfig = config.getCustomerCreate();
        TimeUnit.NANOSECONDS.sleep(createConfig.getDelay().getNano());

        return CustomerCreateResponse.builder()
                .bdpUuid(UUID.randomUUID())
                .fnumber("F123456")
                .build();
    }
}
