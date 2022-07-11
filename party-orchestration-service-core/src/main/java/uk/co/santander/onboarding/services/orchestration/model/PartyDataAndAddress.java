package uk.co.santander.onboarding.services.orchestration.model;

import java.util.Collection;
import java.util.Optional;
import lombok.Builder;
import lombok.Value;
import uk.co.santander.onboarding.services.address.address.dto.AddressDTO;
import uk.co.santander.onboarding.services.party.dto.ApplicantDTO;

/**
 * POJO to store information about both party address and data.
 */
@Value
@Builder
public class PartyDataAndAddress {
    Optional<ApplicantDTO> applicantOptional;
    Collection<AddressDTO> addresses;
}
