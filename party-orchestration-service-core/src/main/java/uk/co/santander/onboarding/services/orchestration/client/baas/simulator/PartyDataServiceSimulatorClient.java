package uk.co.santander.onboarding.services.orchestration.client.baas.simulator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.santander.onboarding.services.orchestration.client.baas.PartyDataServiceClient;
import uk.co.santander.onboarding.services.orchestration.client.core.DummyImplementation;
import uk.co.santander.onboarding.services.party.dto.ApplicantDTO;

import java.util.Optional;
import java.util.UUID;

/**
 * Client which uses outer world simulator.
 */
@Service
@DummyImplementation
public class PartyDataServiceSimulatorClient implements PartyDataServiceClient {
    @Autowired
    private PartyDataFeignClient feignClient;

    @Override
    public Optional<ApplicantDTO> findById(UUID applicantId) {
        return Optional.ofNullable(feignClient.getApplicant(applicantId));
    }
}
