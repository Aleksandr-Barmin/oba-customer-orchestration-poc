package uk.co.santander.onboarding.services.orchestration.client;

import uk.co.santander.onboarding.services.party.dto.ApplicantDTO;

import java.util.Optional;
import java.util.UUID;

/**
 * This is an abstraction for the party data service client.
 */
public interface PartyDataServiceClient {
    /**
     * Find information about an applicant by its id.
     *
     * @param applicantId
     * @return
     */
    Optional<ApplicantDTO> findById(UUID applicantId);
}
