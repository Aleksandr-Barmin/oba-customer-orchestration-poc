package uk.co.santander.onboarding.core.client.search;

import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is a dummy request, should be updated with real fields.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSearchRequest {
    /**
     * Identifier of an application to search for. Should be next replaced with meaningful fields like
     * first name, last name, some address information.
     */
    @NotNull
    private UUID applicantId;
}
