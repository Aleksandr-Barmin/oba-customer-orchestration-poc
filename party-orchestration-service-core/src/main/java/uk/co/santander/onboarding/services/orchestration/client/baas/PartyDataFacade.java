package uk.co.santander.onboarding.services.orchestration.client.baas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.santander.onboarding.services.address.address.dto.AddressDTO;
import uk.co.santander.onboarding.services.orchestration.model.PartyDataAndAddress;
import uk.co.santander.onboarding.services.party.dto.ApplicantDTO;
import uk.co.santander.onboarding.services.party.dto.ContactPointDTO;
import uk.co.santander.onboarding.services.party.dto.PostalAddressesDTO;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PartyDataFacade {
    @Autowired
    private PartyDataServiceClient dataServiceClient;

    @Autowired
    private PartyAddressServiceClient addressServiceClient;

    public PartyDataAndAddress getPartyData(final UUID applicantId) {
        final Optional<ApplicantDTO> applicantOptional = dataServiceClient.findById(applicantId);

        final Collection<AddressDTO> addressCollection = applicantOptional.map(ApplicantDTO::getContactPoint)
                .map(ContactPointDTO::getPostalAddresses)
                .map(this::getPartyAddress)
                .orElse(List.of());

        return PartyDataAndAddress.builder()
                .applicantOptional(applicantOptional)
                .addresses(addressCollection)
                .build();
    }

    private Collection<AddressDTO> getPartyAddress(final Collection<PostalAddressesDTO> addressesDTOS) {
        return addressesDTOS.stream()
                .map(PostalAddressesDTO::getAddressId)
                .map(addressServiceClient::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
