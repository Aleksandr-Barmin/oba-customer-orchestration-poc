package uk.co.santander.onboarding.services.orchestration.client.baas;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uk.co.santander.onboarding.services.address.address.dto.AddressDTO;
import uk.co.santander.onboarding.services.orchestration.config.CacheNames;
import uk.co.santander.onboarding.services.orchestration.model.PartyDataAndAddress;
import uk.co.santander.onboarding.services.party.dto.ApplicantDTO;
import uk.co.santander.onboarding.services.party.dto.ContactPointDTO;
import uk.co.santander.onboarding.services.party.dto.PostalAddressesDTO;

/**
 * This is a more high-level abstraction over services which are getting data about applicant. The
 * reason to have it is that we need to get this data in one go and in order to proceed need info
 * about both address and applicant itself.
 */
@Service
public class PartyDataFacade {
    @Autowired
    private PartyDataServiceClient dataServiceClient;

    @Autowired
    private PartyAddressServiceClient addressServiceClient;

    /**
     * Get information about both applicant and its address in one go. The data is cached for
     * subsequent calls.
     *
     * @param applicantId is an identifier of an applicant to retrieve.
     * @return party and address data as one object.
     */
    @Cacheable(CacheNames.APPLICANT_DATA)
    public PartyDataAndAddress getPartyData(final UUID applicantId) {
        final Optional<ApplicantDTO> applicantOptional = dataServiceClient.findById(applicantId);

        final Collection<AddressDTO> addressCollection =
                applicantOptional
                        .map(ApplicantDTO::getContactPoint)
                        .map(ContactPointDTO::getPostalAddresses)
                        .map(this::getPartyAddress)
                        .orElse(List.of());

        return PartyDataAndAddress.builder()
                .applicantOptional(applicantOptional)
                .addresses(addressCollection)
                .build();
    }

    private Collection<AddressDTO> getPartyAddress(
            final Collection<PostalAddressesDTO> addressesDtos) {
        return addressesDtos.stream()
                .map(PostalAddressesDTO::getAddressId)
                .map(addressServiceClient::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
