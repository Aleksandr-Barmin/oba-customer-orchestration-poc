package uk.co.santander.onboarding.core.client.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

/** Response from the Customer Create Core API. */
@Data
@Builder
public class CustomerCreateResponse {
  /** UUID in BDP of newly created record. */
  @JsonProperty("bdpUuid")
  private UUID bdpUuid;

  /** F-Number of a created customer. */
  @JsonProperty("fNumber")
  private String fnumber;
}
