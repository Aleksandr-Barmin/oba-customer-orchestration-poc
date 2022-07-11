package uk.co.santander.onboarding.services.orchestration.client.baas;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.co.santander.onboarding.services.address.address.dto.AddressDTO;
import uk.co.santander.onboarding.services.orchestration.model.PartyDataAndAddress;
import uk.co.santander.onboarding.services.party.dto.ApplicantDTO;
import uk.co.santander.onboarding.services.party.dto.ContactPointDTO;
import uk.co.santander.onboarding.services.party.dto.PostalAddressesDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        PartyDataFacade.class
})
class PartyDataFacadeTest {
    @Autowired
    private PartyDataFacade uut;

    @MockBean
    private PartyDataServiceClient dataServiceClient;

    @MockBean
    private PartyAddressServiceClient addressServiceClient;

    @Test
    @DisplayName("Context should start")
    void check_contextStarts() {
        assertThat(uut).isNotNull()
                .withFailMessage("Context should start");
    }

    @Test
    @DisplayName("Get party data should always return party data")
    void getPartyData_shouldAlwaysReturnSomething() {
        final PartyDataAndAddress partyData = uut.getPartyData(UUID.randomUUID());
        assertThat(partyData).isNotNull()
                .withFailMessage("Party data should be always returned");
    }

    @Test
    @DisplayName("Get party data should use party data service")
    void getPartyData_shouldAccessPartyDataService() {
        final UUID applicantId = UUID.randomUUID();

        when(dataServiceClient.findById(eq(applicantId))).thenReturn(Optional.of(
                ApplicantDTO.builder().build()
        ));

        final PartyDataAndAddress partyData = uut.getPartyData(applicantId);

        assertThat(partyData).isNotNull()
                .withFailMessage("Party data is null");

        assertThat(partyData.getApplicantOptional())
                .isNotNull()
                .isNotEmpty()
                .withFailMessage("Applicant data should be provided");

        verify(dataServiceClient, times(1)).findById(eq(applicantId));
    }

    @Test
    @DisplayName("Get party data should use party address service")
    void getPartyData_shouldUsePartyAddressService() {
        final UUID applicantId = UUID.randomUUID();
        final UUID addressId = UUID.randomUUID();

        when(dataServiceClient.findById(eq(applicantId))).thenReturn(Optional.of(
                ApplicantDTO.builder()
                        .contactPoint(ContactPointDTO.builder()
                                .postalAddresses(List.of(
                                        PostalAddressesDTO.builder()
                                                .addressId(addressId)
                                                .build()
                                ))
                                .build())
                        .build()
        ));
        when(addressServiceClient.findById(eq(addressId))).thenReturn(Optional.of(
                AddressDTO.builder()
                        .build()
        ));

        final PartyDataAndAddress partyData = uut.getPartyData(applicantId);

        assertThat(partyData).isNotNull()
                .withFailMessage("Party data should be returned");

        assertThat(partyData.getApplicantOptional())
                .isNotNull()
                .isNotEmpty()
                .withFailMessage("Applicant data should be returned");

        assertThat(partyData.getAddresses())
                .isNotNull()
                .isNotEmpty()
                .withFailMessage("Applicant address should be returned");

        verify(dataServiceClient, times(1)).findById(eq(applicantId));
        verify(addressServiceClient, times(1)).findById(eq(addressId));
    }
}