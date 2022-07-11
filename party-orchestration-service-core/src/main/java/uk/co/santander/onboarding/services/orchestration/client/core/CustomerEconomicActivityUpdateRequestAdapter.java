package uk.co.santander.onboarding.services.orchestration.client.core;

import java.util.UUID;
import org.springframework.stereotype.Component;
import uk.co.santander.onboarding.core.client.economic.data.CustomerEconomicDataUpdateRequest;
import uk.co.santander.onboarding.services.orchestration.model.PartyDataAndAddress;

/**
 * This is an adapter which generate requests for the {@link
 * uk.co.santander.onboarding.core.client.economic.data.CustomerUpdateEconomicDataClient}.
 *
 * <p>The implementation is silly and dummy, so should be rewritten later.
 *
 * <p>Name also should be updated, because it is too long.
 */
@Component
@DummyImplementation
public class CustomerEconomicActivityUpdateRequestAdapter {
    /**
     * Create a request to be sent to Core API.
     *
     * @param bdpUuid   identifier of a customer in BDP.
     * @param partyData party data to build request.
     * @return request for the Core API.
     */
    public CustomerEconomicDataUpdateRequest create(
            final UUID bdpUuid, final PartyDataAndAddress partyData) {

        return CustomerEconomicDataUpdateRequest.builder()
                .bdpUuid(bdpUuid)
                .economicData(partyData.toString())
                .build();
    }
}
