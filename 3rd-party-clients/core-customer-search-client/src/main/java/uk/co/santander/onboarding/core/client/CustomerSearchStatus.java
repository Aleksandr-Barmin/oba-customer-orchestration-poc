package uk.co.santander.onboarding.core.client;

public enum CustomerSearchStatus {
    /**
     * Customer found in BDP.
     */
    FOUND_SINGLE,

    /**
     * Multiple customers found with the given data.
     */
    FOUND_MULTIPLE,

    /**
     * Customer not found in BDP.
     */
    NOT_FOUND
}
