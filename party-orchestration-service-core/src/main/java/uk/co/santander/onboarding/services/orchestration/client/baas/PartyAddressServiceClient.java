package uk.co.santander.onboarding.services.orchestration.client.baas;

import uk.co.santander.onboarding.services.address.address.dto.AddressDTO;

import java.util.Optional;
import java.util.UUID;

/**
 * Abstraction to access the party address service.
 */
public interface PartyAddressServiceClient {
    /**
     * Find address by id.
     *
     * @param addressId
     * @return
     */
    Optional<AddressDTO> findById(UUID addressId);
}
