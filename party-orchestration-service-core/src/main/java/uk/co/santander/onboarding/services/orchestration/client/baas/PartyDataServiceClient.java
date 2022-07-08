package uk.co.santander.onboarding.services.orchestration.client.baas;

import java.util.Optional;
import java.util.UUID;
import uk.co.santander.onboarding.services.party.dto.ApplicantDTO;

/** This is an abstraction for the party data service client. */
public interface PartyDataServiceClient {
  /**
   * Find information about an applicant by its id.
   *
   * @param applicantId
   * @return
   */
  Optional<ApplicantDTO> findById(UUID applicantId);
}
