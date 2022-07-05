package uk.co.santander.onboarding.services.orchestration.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ApplicationInfo {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    private UUID applicationId;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    private String status; // TODO: I think it should be enum but not string
    private String additionalData;
}
