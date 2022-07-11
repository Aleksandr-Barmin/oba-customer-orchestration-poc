package uk.co.santander.onboarding.core.client.economic.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response from Core API. Should be updated when real Core API client is available.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEconomicDataUpdateResponse {
    private String status;
}
