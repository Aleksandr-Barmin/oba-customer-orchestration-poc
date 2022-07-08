package uk.co.santander.onboarding.services.orchestration.service;

import java.util.Collection;
import uk.co.santander.onboarding.services.orchestration.model.ApplicationInfo;

public interface ApplicationRepository {
  ApplicationInfo save(ApplicationInfo info);

  Collection<ApplicationInfo> findAll();
}
