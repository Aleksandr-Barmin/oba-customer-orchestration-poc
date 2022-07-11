package uk.co.santander.onboarding.services.orchestration.core;

import io.swagger.annotations.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.santander.onboarding.core.client.create.CustomerCreateRequest;
import uk.co.santander.onboarding.core.client.create.CustomerCreateResponse;
import uk.co.santander.onboarding.core.client.economic.data.CustomerEconomicDataUpdateRequest;
import uk.co.santander.onboarding.core.client.economic.data.CustomerEconomicDataUpdateResponse;
import uk.co.santander.onboarding.core.client.search.CustomerSearchRequest;
import uk.co.santander.onboarding.core.client.search.CustomerSearchResponse;
import uk.co.santander.onboarding.core.client.search.CustomerSearchStatus;
import uk.co.santander.onboarding.services.orchestration.config.WorldSimulatorConfig;

/**
 * Core API simulator.
 */
@RestController
@RequestMapping("/core/customer")
@Tag(name = "Core API", description = "Core API Simulators")
public class CoreApiSimulationController {
    @Autowired
    private WorldSimulatorConfig config;

    /**
     * Simulates Customer Search Core API.
     *
     * @param request for the Core API.
     * @return response of the Core API.
     */
    @SneakyThrows
    @PostMapping("/search")
    @Operation(summary = "Search for a customer")
    @ApiResponse(
            code = 200,
            response = CustomerSearchResponse.class,
            message = "Result of customer search")
    public CustomerSearchResponse searchCustomer(
            final @RequestBody @Valid CustomerSearchRequest request) {
        final WorldSimulatorConfig.CoreCustomerSearch searchConfig = config.getCustomerSearch();
        TimeUnit.NANOSECONDS.sleep(searchConfig.getDelay().getNano());

        if (searchConfig.isFound()) {
            return CustomerSearchResponse.builder()
                    .status(CustomerSearchStatus.FOUND_SINGLE)
                    .bdpUuid(UUID.randomUUID())
                    .fnumber("F123456")
                    .build();
        }
        return CustomerSearchResponse.builder().status(CustomerSearchStatus.NOT_FOUND).build();
    }

    /**
     * Simulate Customer Create Core API.
     *
     * @param request for Core API.
     * @return response of Core API.
     */
    @SneakyThrows
    @PostMapping("/create")
    @Operation(summary = "Create a new customer")
    @ApiResponse(
            code = 200,
            response = CustomerCreateResponse.class,
            message = "Result of customer creation")
    public CustomerCreateResponse createCustomer(
            final @RequestBody @Valid CustomerCreateRequest request) {
        final WorldSimulatorConfig.CoreCustomerCreate createConfig = config.getCustomerCreate();
        TimeUnit.NANOSECONDS.sleep(createConfig.getDelay().getNano());

        return CustomerCreateResponse.builder().bdpUuid(UUID.randomUUID()).fnumber("F123456").build();
    }

    @SneakyThrows
    @PostMapping("/update/{bdpUuid}")
    public CustomerEconomicDataUpdateResponse updateEconomicData(
            final @PathVariable("bdpUuid") UUID bpdUuid,
            final @RequestBody @Valid CustomerEconomicDataUpdateRequest request) {
        final WorldSimulatorConfig.CoreCustomerUpdate updateConfig = config.getCustomerUpdate();
        TimeUnit.NANOSECONDS.sleep(updateConfig.getDelay().getNano());

        return CustomerEconomicDataUpdateResponse.builder()
                .status("Updated")
                .build();
    }
}
