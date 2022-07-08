package uk.co.santander.onboarding.services.orchestration.client.core;

import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.core.client.search.CustomerSearchRequest;
import uk.co.santander.onboarding.services.orchestration.model.PartyDataAndAddress;

/** This component converts party data and address to the request for core API. */
@Component
@DummyImplementation
public class CustomerSearchRequestAdapter {
  /**
   * Build a request for sending to the Customer Search Core API.
   *
   * @param dataAndAddress information about applicant.
   * @return request for the Core API.
   */
  public CustomerSearchRequest build(final PartyDataAndAddress dataAndAddress) {
    return CustomerSearchRequest.builder()
        .applicantId(dataAndAddress.getApplicantOptional().get().getApplicantId())
        .build();
  }
}
