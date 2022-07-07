package uk.co.santander.onboarding.services.orchestration.model;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
public class ApplicationInfoRecord {
    private String applicationId;
    private Collection<ApplicationInfo> records;
}
