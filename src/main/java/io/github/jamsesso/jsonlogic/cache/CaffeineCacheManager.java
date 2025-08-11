package io.github.jamsesso.jsonlogic.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * CaffeineCacheManager is a cache manager that uses Caffeine for caching.
 * It provides methods to check if a key exists, put a value in the cache,
 * and retrieve a value from the cache.
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of cached values
 */
public class CaffeineCacheManager<K, V> implements CacheManager<K, V> {
  private final Cache<K, V> cache;

  public CaffeineCacheManager() {
    this.cache =  Caffeine.newBuilder().build();
  }

  /**
   * Constructs a CaffeineCacheManager with the specified maximum capacity.
   *
   * @param maxCapacity the maximum number of entries the cache can hold
   */
  public CaffeineCacheManager(int maxCapacity) {
    this.cache = Caffeine.newBuilder().maximumSize(maxCapacity).build();
  }

  @Override
  public boolean containsKey(K key) {
    return cache.asMap().containsKey(key);
  }

  @Override
  public void put(K key, V value) {
    cache.put(key, value);
  }

  @Override
  public V get(K key) {
    return cache.getIfPresent(key);
  }
}