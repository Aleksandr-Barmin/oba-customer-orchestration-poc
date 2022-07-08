package uk.co.santander.onboarding.core.client.create;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * Response from the Customer Create Core API.
 */
@Data
@Builder
public class CustomerCreateResponse {
    /**
     * UUID in BDP of newly created record.
     */
    private UUID bdpUuid;

    /**
     * F-Number of a created customer.
     */
    private String fNumber;
}
