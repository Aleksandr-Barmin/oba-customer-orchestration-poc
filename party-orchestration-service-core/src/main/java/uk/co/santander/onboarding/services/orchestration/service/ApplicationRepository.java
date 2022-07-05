package uk.co.santander.onboarding.services.orchestration.service;

import uk.co.santander.onboarding.services.orchestration.model.ApplicationInfo;

import java.util.Collection;

public interface ApplicationRepository {
    ApplicationInfo save(ApplicationInfo info);

    Collection<ApplicationInfo> findAll();
}
