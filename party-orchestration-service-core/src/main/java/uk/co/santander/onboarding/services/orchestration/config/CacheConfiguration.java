package uk.co.santander.onboarding.services.orchestration.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configuration for Spring Cache. */
@Configuration(proxyBeanMethods = false)
public class CacheConfiguration {
  /**
   * Temporarily use concurrent hash map as caching backend.
   *
   * @return a configured cache manager.
   */
  @Bean
  public CacheManager cacheManager() {
    return new ConcurrentMapCacheManager();
  }
}
