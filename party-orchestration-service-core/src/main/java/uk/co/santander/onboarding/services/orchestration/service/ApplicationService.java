package uk.co.santander.onboarding.services.orchestration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.santander.onboarding.services.orchestration.model.ApplicationInfo;

import java.util.UUID;

@Service
public class ApplicationService {
    @Autowired
    private ApplicationRepository repository;

    public ApplicationInfo createRecord(UUID applicantId, String status) {
        final ApplicationInfo appInfo = ApplicationInfo.builder()
                .applicationId(applicantId)
                .status(status)
                .build();

        return repository.save(appInfo);
    }
}
