package uk.co.santander.onboarding.services.orchestration.client.baas.simulator;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uk.co.santander.onboarding.services.party.dto.ApplicantDTO;

import java.util.UUID;

@FeignClient(url = "${client.party-data.base-url}", name = "party-data-client")
public interface PartyDataFeignClient {
    @GetMapping("/{applicantId}")
    ApplicantDTO getApplicant(@PathVariable("applicantId") UUID applicantId);
}
