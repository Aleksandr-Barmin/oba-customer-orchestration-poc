package uk.co.santander.onboarding.services.orchestration.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class ApplicationStartRequest {
    @NotNull
    private UUID applicantId;

    @NotNull
    private ApplicationChannel channel;

    @NotNull
    private CustomerType customerType;
}
