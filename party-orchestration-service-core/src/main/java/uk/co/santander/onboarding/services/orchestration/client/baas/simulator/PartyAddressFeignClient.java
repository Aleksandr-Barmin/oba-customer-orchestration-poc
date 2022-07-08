package uk.co.santander.onboarding.services.orchestration.client.baas.simulator;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uk.co.santander.onboarding.services.address.address.dto.AddressDTO;

@FeignClient(url = "${client.party-address.base-url}", name = "party-address-client")
public interface PartyAddressFeignClient {
  @GetMapping("/{addressId}")
  AddressDTO getAddress(@PathVariable("addressId") UUID addressId);
}
