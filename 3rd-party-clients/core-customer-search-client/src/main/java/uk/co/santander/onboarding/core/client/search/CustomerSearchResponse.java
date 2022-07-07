package uk.co.santander.onboarding.core.client.search;

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

    public static CustomerSearchResponse notFound() {
        return CustomerSearchResponse.builder()
                .status(CustomerSearchStatus.NOT_FOUND)
                .build();
    }
}
