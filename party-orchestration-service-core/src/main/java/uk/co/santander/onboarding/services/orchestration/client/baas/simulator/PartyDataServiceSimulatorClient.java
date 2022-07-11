package uk.co.santander.onboarding.services.orchestration.client.baas.simulator;

import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.santander.onboarding.services.orchestration.client.baas.PartyDataServiceClient;
import uk.co.santander.onboarding.services.orchestration.client.core.DummyImplementation;
import uk.co.santander.onboarding.services.party.dto.ApplicantDTO;

/**
 * Client which uses outer world simulator.
 */
@Slf4j
@Service
@DummyImplementation
public class PartyDataServiceSimulatorClient implements PartyDataServiceClient {
    @Autowired
    private PartyDataFeignClient feignClient;

    @Override
    public Optional<ApplicantDTO> findById(UUID applicantId) {
        try {
            return Optional.ofNullable(feignClient.getApplicant(applicantId));
        } catch (Exception e) {
            log.error("Error occurred while getting applicant", e);
            return Optional.empty();
        }
    }
}
