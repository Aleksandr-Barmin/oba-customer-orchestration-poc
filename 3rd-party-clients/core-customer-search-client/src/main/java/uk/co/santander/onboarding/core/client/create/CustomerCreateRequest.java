package uk.co.santander.onboarding.core.client.create;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CustomerCreateRequest {
    private UUID applicantId;
}
