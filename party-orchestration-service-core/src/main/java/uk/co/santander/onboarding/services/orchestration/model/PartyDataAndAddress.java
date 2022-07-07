package uk.co.santander.onboarding.services.orchestration.model;

import lombok.Builder;
import lombok.Value;
import uk.co.santander.onboarding.services.address.address.dto.AddressDTO;
import uk.co.santander.onboarding.services.party.dto.ApplicantDTO;

import java.util.Collection;
import java.util.Optional;

@Value
@Builder
public class PartyDataAndAddress {
    Optional<ApplicantDTO> applicantOptional;
    Collection<AddressDTO> addresses;
}
