package uk.co.santander.onboarding.core.client;

import lombok.Data;

import java.util.UUID;

/**
 * This is a dummy response, should be updated with real values.
 */
@Data
public class CustomerSearchResponse {
    private CustomerSearchStatus status;
    private String fNumber;
    private UUID bdpUuid;
}
