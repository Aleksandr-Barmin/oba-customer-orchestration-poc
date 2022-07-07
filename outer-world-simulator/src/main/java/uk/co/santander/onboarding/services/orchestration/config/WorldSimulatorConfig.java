package uk.co.santander.onboarding.services.orchestration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Duration;

@Data
@Validated
@ConfigurationProperties(prefix = "world.config")
public class WorldSimulatorConfig {
    @Valid
    private CoreCustomerSearch customerSearch = new CoreCustomerSearch();

    @Valid
    private CoreCustomerCreate customerCreate = new CoreCustomerCreate();

    @Valid
    private BaasPartyDataSearch partyDataSearch = new BaasPartyDataSearch();

    @Valid
    private BaasPartyAddressSearch partyAddressSearch = new BaasPartyAddressSearch();

    @Data
    public static class CoreCustomerCreate {
        @NotNull
        private Duration delay;
    }

    @Data
    public static class CoreCustomerSearch {
        private boolean found;

        @NotNull
        private Duration delay;
    }

    @Data
    public static class BaasPartyDataSearch {
        private boolean found;

        @NotNull
        private Duration delay;
    }

    @Data
    public static class BaasPartyAddressSearch {
        private boolean found;

        @NotNull
        private Duration delay;
    }
}
