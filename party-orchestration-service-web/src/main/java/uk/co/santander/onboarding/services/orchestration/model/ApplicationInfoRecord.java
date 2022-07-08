package uk.co.santander.onboarding.services.orchestration.model;

import java.util.Collection;
import lombok.Builder;
import lombok.Data;

/** Model used for web view. */
@Data
@Builder
public class ApplicationInfoRecord {
  private String applicationId;
  private Collection<ApplicationInfo> records;
}
