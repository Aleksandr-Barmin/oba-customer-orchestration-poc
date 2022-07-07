package uk.co.santander.onboarding.core.client.search;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

    @Getter
    private final boolean found;
}
