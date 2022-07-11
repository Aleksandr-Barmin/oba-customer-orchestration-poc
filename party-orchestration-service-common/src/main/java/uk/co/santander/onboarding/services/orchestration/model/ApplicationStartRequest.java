package uk.co.santander.onboarding.services.orchestration.model;

import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for the request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationStartRequest {
    /**
     * Identifier of the applicant.
     */
    @NotNull
    private UUID applicantId;

    /**
     * Application channel.
     */
    @NotNull
    private ApplicationChannel channel;

    /**
     * Customer type.
     */
    @NotNull
    private CustomerType customerType;
}
