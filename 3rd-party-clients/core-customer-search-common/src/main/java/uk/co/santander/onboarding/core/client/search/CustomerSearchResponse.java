package uk.co.santander.onboarding.core.client.search;

import lombok.Builder;
import lombok.Data;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * This is a dummy response, should be updated with real values.
 */
@Data
@Builder
public class CustomerSearchResponse {
    /**
     * Status of the search result.
     */
    private CustomerSearchStatus status;

    /**
     * F-Number of a customer in BDP (if found).
     */
    @Nullable
    private String fNumber;

    /**
     * UUID of a customer in BDP (if found).
     */
    @Nullable
    private UUID bdpUuid;

    /**
     * Create a new response with not-found status.
     * @return a new customer search response.
     */
    public static CustomerSearchResponse notFound() {
        return CustomerSearchResponse.builder()
                .status(CustomerSearchStatus.NOT_FOUND)
                .build();
    }
}
