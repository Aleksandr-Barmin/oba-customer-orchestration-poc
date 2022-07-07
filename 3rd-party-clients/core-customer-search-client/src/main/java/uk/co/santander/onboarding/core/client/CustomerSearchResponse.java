package uk.co.santander.onboarding.core.client;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * This is a dummy response, should be updated with real values.
 */
@Data
@Builder
public class CustomerSearchResponse {
    private CustomerSearchStatus status;
    private String fNumber;
    private UUID bdpUuid;
}
