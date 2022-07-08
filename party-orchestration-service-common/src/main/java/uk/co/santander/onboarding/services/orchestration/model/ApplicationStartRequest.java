package uk.co.santander.onboarding.services.orchestration.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * DTO for the request.
 */
@Data
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
