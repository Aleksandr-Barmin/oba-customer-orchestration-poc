package uk.co.santander.onboarding.services.orchestration.client.baas.simulator;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uk.co.santander.onboarding.services.party.dto.ApplicantDTO;

@FeignClient(url = "${client.party-data.base-url}", name = "party-data-client")
public interface PartyDataFeignClient {
  @GetMapping("/{applicantId}")
  ApplicantDTO getApplicant(@PathVariable("applicantId") UUID applicantId);
}
