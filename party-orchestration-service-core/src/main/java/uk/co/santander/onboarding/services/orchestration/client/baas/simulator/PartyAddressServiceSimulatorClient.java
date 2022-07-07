package uk.co.santander.onboarding.services.orchestration.client.baas.simulator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.santander.onboarding.services.address.address.dto.AddressDTO;
import uk.co.santander.onboarding.services.orchestration.client.baas.PartyAddressServiceClient;
import uk.co.santander.onboarding.services.orchestration.client.core.DummyImplementation;

import java.util.Optional;
import java.util.UUID;

@Service
@DummyImplementation
public class PartyAddressServiceSimulatorClient implements PartyAddressServiceClient {
    @Autowired
    private PartyAddressFeignClient feignClient;

    @Override
    public Optional<AddressDTO> findById(UUID addressId) {
        return Optional.ofNullable(feignClient.getAddress(addressId));
    }
}
