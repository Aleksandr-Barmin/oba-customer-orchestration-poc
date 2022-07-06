package uk.co.santander.onboarding.services.orchestration.state.validator;

import lombok.Value;
import uk.co.santander.onboarding.services.address.address.dto.AddressDTO;
import uk.co.santander.onboarding.services.party.dto.ApplicantDTO;

import java.util.Optional;

@Value
public class PartyDataAndAddress {
    Optional<ApplicantDTO> applicantOptional;
    Optional<AddressDTO> addressOptional;
}
