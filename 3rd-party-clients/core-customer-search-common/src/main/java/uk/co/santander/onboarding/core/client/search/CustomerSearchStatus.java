package uk.co.santander.onboarding.core.client.search;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Status of the customer search - if customer is found or not.
 */
@RequiredArgsConstructor
public enum CustomerSearchStatus {
    /**
     * Customer found in BDP.
     */
    FOUND_SINGLE(true),

    /**
     * Multiple customers found with the given data.
     */
    FOUND_MULTIPLE(true),

    /**
     * Customer not found in BDP.
     */
    NOT_FOUND(false);

    /**
     * If the customer actually found or not, for case when multiple matches are found or for cases
     * when error happened during the check.
     */
    @Getter
    private final boolean found;
}
