package uk.co.santander.onboarding.core.client.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * DTO that represents a request for Core API to create a new customer.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCreateRequest {
    /**
     * Applicant id for now. Next it should be replaced with meaningful
     * set of fields based on contract.
     */
    @NotNull
    private UUID applicantId;
}
