package uk.co.santander.onboarding.services.orchestration.persistence;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import uk.co.santander.onboarding.services.orchestration.model.ApplicationInfo;
import uk.co.santander.onboarding.services.orchestration.service.ApplicationRepository;

public class InMemoryApplicationRepository implements ApplicationRepository {
  private final List<ApplicationInfo> records = Lists.newArrayList();

  @Override
  public ApplicationInfo save(ApplicationInfo info) {
    records.add(info);
    return info;
  }

  @Override
  public Collection<ApplicationInfo> findAll() {
    return records;
  }
}
