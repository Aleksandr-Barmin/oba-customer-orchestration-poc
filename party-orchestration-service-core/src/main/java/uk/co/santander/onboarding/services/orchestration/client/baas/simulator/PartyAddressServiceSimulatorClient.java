package uk.co.santander.onboarding.services.orchestration.client.baas.simulator;

import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.santander.onboarding.services.address.address.dto.AddressDTO;
import uk.co.santander.onboarding.services.orchestration.client.baas.PartyAddressServiceClient;
import uk.co.santander.onboarding.services.orchestration.client.core.DummyImplementation;

@Slf4j
@Service
@DummyImplementation
public class PartyAddressServiceSimulatorClient implements PartyAddressServiceClient {
  @Autowired private PartyAddressFeignClient feignClient;

  @Override
  public Optional<AddressDTO> findById(UUID addressId) {
    try {
      return Optional.ofNullable(feignClient.getAddress(addressId));
    } catch (Exception e) {
      log.error("Error occurred while getting address", e);
      return Optional.empty();
    }
  }
}
