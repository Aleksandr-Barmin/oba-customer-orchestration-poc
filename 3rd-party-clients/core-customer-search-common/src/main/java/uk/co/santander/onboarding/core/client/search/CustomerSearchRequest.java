package uk.co.santander.onboarding.core.client.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * This is a dummy request, should be updated with real fields.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSearchRequest {
    @NotNull
    private UUID applicantId;
}
