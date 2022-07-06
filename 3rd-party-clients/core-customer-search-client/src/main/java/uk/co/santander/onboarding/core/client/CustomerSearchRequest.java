package uk.co.santander.onboarding.core.client;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * This is a dummy request, should be updated with real fields.
 */
@Data
@Builder
public class CustomerSearchRequest {
    private UUID applicantId;
}
