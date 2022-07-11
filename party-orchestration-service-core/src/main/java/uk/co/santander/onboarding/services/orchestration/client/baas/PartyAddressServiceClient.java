package uk.co.santander.onboarding.services.orchestration.client.baas;

import java.util.Optional;
import java.util.UUID;
import uk.co.santander.onboarding.services.address.address.dto.AddressDTO;

/**
 * Abstraction to access the party address service.
 */
public interface PartyAddressServiceClient {
    /**
     * Find address by id.
     *
     * @param addressId address identifier.
     * @return optional of the address - address may not be found.
     */
    Optional<AddressDTO> findById(UUID addressId);
}
