package uk.co.santander.onboarding.services.orchestration.client.core;

import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.core.client.create.CustomerCreateRequest;
import uk.co.santander.onboarding.services.orchestration.model.PartyDataAndAddress;

@Component
public class CustomerCreateRequestAdapter {
  @DummyImplementation
  public CustomerCreateRequest build(final PartyDataAndAddress partyData) {
    return CustomerCreateRequest.builder()
        .applicantId(partyData.getApplicantOptional().get().getApplicantId())
        .build();
  }
}
