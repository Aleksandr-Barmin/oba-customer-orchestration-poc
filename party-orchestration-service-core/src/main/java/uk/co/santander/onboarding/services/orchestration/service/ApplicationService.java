package uk.co.santander.onboarding.services.orchestration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import uk.co.santander.onboarding.services.orchestration.model.ApplicationInfo;

@Service
@RequiredArgsConstructor
public class ApplicationService {
  private final ApplicationRepository repository;
  private final ObjectMapper objectMapper;

  /**
   * Create an application info record without additional payload.
   *
   * @param applicantId this record belongs to.
   * @param status of the record, some short, one-line description.
   * @return created application info record.
   */
  public ApplicationInfo createRecord(final UUID applicantId, final String status) {

    final ApplicationInfo appInfo =
        ApplicationInfo.builder().applicationId(applicantId).status(status).build();

    return repository.save(appInfo);
  }

  /**
   * Create an application info record with additional payload.
   *
   * @param applicantId this record belongs to.
   * @param status of the record, some short, one-line description.
   * @param payload to be added to the record.
   * @return created application info record.
   */
  @SneakyThrows
  public ApplicationInfo createRecord(
      final UUID applicantId, final String status, final Object payload) {

    Objects.requireNonNull(payload, "Payload should not be null");
    final ApplicationInfo applicationInfo =
        ApplicationInfo.builder()
            .status(status)
            .applicationId(applicantId)
            .additionalData(objectMapper.writeValueAsString(payload))
            .build();

    return repository.save(applicationInfo);
  }
}
