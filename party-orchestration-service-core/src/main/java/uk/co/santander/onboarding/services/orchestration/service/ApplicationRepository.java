package uk.co.santander.onboarding.services.orchestration.service;

import java.util.Collection;
import uk.co.santander.onboarding.services.orchestration.model.ApplicationInfo;

/**
 * Repository to store information about business events happened during the on-boarding process.
 */
public interface ApplicationRepository {
    ApplicationInfo save(ApplicationInfo info);

    Collection<ApplicationInfo> findAll();
}
