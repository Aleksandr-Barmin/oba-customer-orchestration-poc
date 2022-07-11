package uk.co.santander.onboarding.core.client.economic.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request to update an economic data using Core API. This class should be updated when real Core
 * API clients are ready.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEconomicDataUpdateRequest {
    /**
     * Identifier of a record in BDP.
     */
    @JsonProperty("bdpUuid")
    private UUID bdpUuid;

    /**
     * Dummy economic data.
     */
    private String economicData;
}
