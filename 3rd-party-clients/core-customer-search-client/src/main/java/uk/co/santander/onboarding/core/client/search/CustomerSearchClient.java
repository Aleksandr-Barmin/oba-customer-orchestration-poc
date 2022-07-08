package uk.co.santander.onboarding.core.client.search;

/** Interface for the customer search client, may have multiple implementations based on needs. */
public interface CustomerSearchClient {
  /**
   * Try to find customer in BDP.
   *
   * @param request search request.
   * @return search results.
   */
  CustomerSearchResponse search(CustomerSearchRequest request);
}
