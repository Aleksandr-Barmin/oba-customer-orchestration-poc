package uk.co.santander.onboarding.services.orchestration.baas;

import io.swagger.annotations.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.santander.onboarding.services.address.address.dto.AddressDTO;
import uk.co.santander.onboarding.services.address.address.dto.PostalAddressDTO;
import uk.co.santander.onboarding.services.orchestration.config.WorldSimulatorConfig;
import uk.co.santander.onboarding.services.party.dto.ApplicantDTO;
import uk.co.santander.onboarding.services.party.dto.ContactPointDTO;
import uk.co.santander.onboarding.services.party.dto.PostalAddressesDTO;

/**
 * BaaS API simulator.
 */
@RestController
@RequestMapping("/party/")
@Tag(name = "BaaS API", description = "BaaS API Simulators")
public class BaasApiSimulationController {
    @Autowired
    private WorldSimulatorConfig config;

    /**
     * Simulate behavior of party data service.
     *
     * @param applicantId identifier of an applicant.
     * @return applicant data or 404 error.
     */
    @SneakyThrows
    @GetMapping("/data/{applicantId}")
    @Operation(summary = "Get an applicant by applicant ID")
    @ApiResponse(code = 200, response = ApplicantDTO.class, message = "Applicant data")
    public ResponseEntity<ApplicantDTO> getApplicant(
            final @PathVariable("applicantId") UUID applicantId) {
        final WorldSimulatorConfig.BaasPartyDataSearch partyDataConfig =
                this.config.getPartyDataSearch();
        TimeUnit.NANOSECONDS.sleep(partyDataConfig.getDelay().getNano());

        if (partyDataConfig.isFound()) {
            final ApplicantDTO applicantDto =
                    ApplicantDTO.builder()
                            .applicantId(applicantId)
                            .contactPoint(
                                    ContactPointDTO.builder()
                                            .postalAddresses(
                                                    List.of(
                                                            PostalAddressesDTO.builder().addressId(UUID.randomUUID()).build()))
                                            .build())
                            .build();

            return ResponseEntity.ok(applicantDto);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Simulate behavior of party address service.
     *
     * @param addressId identifier of an address.
     * @return address data or 404 if not found.
     */
    @SneakyThrows
    @GetMapping("/address/{addressId}")
    @Operation(summary = "Get an address by address ID")
    @ApiResponse(code = 200, response = AddressDTO.class, message = "Address data")
    public ResponseEntity<AddressDTO> getAddress(final @PathVariable("addressId") UUID addressId) {
        final WorldSimulatorConfig.BaasPartyAddressSearch partyAddressConfig =
                config.getPartyAddressSearch();
        TimeUnit.NANOSECONDS.sleep(partyAddressConfig.getDelay().getNano());

        if (partyAddressConfig.isFound()) {
            final AddressDTO addressDto =
                    AddressDTO.builder()
                            .applicant(
                                    uk.co.santander.onboarding.services.address.address.dto.ApplicantDTO.builder()
                                            .applicantId(UUID.randomUUID())
                                            .build())
                            .postalAddress(PostalAddressDTO.builder().addressId(addressId).build())
                            .build();

            return ResponseEntity.ok(addressDto);
        }
        return ResponseEntity.notFound().build();
    }
}
