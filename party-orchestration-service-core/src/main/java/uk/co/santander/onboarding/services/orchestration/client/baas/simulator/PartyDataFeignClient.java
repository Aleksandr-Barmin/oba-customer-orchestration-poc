package uk.co.santander.onboarding.services.orchestration.client.baas.simulator;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uk.co.santander.onboarding.services.party.dto.ApplicantDTO;

/** Feign client for the party data service emulated by outer world simulator. */
@FeignClient(url = "${client.party-data.base-url}", name = "party-data-client")
public interface PartyDataFeignClient {
  /**
   * Get an applicant by its id.
   *
   * @param applicantId of an applicant.
   * @return retrieved applicant.
   */
  @GetMapping("/{applicantId}")
  ApplicantDTO getApplicant(@PathVariable("applicantId") UUID applicantId);
}
