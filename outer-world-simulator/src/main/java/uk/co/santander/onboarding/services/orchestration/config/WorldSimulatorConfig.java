package uk.co.santander.onboarding.services.orchestration.config;

import java.time.Duration;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration of the world simulator.
 */
@Data
@Validated
@ConfigurationProperties(prefix = "world.config")
public class WorldSimulatorConfig {
    @Valid
    private CoreCustomerSearch customerSearch = new CoreCustomerSearch();

    @Valid
    private CoreCustomerCreate customerCreate = new CoreCustomerCreate();

    @Valid
    private CoreCustomerUpdate customerUpdate = new CoreCustomerUpdate();

    @Valid
    private BaasPartyDataSearch partyDataSearch = new BaasPartyDataSearch();

    @Valid
    private BaasPartyAddressSearch partyAddressSearch = new BaasPartyAddressSearch();

    /**
     * Configuration for Customer Create Core API.
     */
    @Data
    public static class CoreCustomerCreate {
        @NotNull
        private Duration delay;
    }

    /**
     * Configuration for Customer Search Core API.
     */
    @Data
    public static class CoreCustomerSearch {
        private boolean found;

        @NotNull
        private Duration delay;
    }

    /**
     * Configuration for Customer Update Economic Data Core API.
     */
    @Data
    public static class CoreCustomerUpdate {
        @NotNull
        private Duration delay;
    }

    /**
     * Configuration for Party Data service.
     */
    @Data
    public static class BaasPartyDataSearch {
        private boolean found;

        @NotNull
        private Duration delay;
    }

    /**
     * Configuration for Party Address service.
     */
    @Data
    public static class BaasPartyAddressSearch {
        private boolean found;

        @NotNull
        private Duration delay;
    }
}
