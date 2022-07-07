package uk.co.santander.onboarding.core.client.create;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CustomerCreateResponse {
    private UUID bdpUuid;
    private String fNumber;
}
