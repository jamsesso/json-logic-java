package io.github.jamsesso.jsonlogic.cache;

/**
 * CacheManager interface for managing cache operations.
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of cached values
 */
public interface CacheManager<K, V> {
  /**
   * Checks if the cache contains a value for the specified key.
   * @param key the key to check
   * @return true if the cache contains the key, false otherwise
   */
  boolean containsKey(K key);

  /**
   * Puts a value in the cache with the specified key.
   * @param key the key to associate with the value
   * @param value the value to cache
   */
  void put(K key, V value);

  /**
   * Retrieves a value from the cache for the specified key.
   * @param key the key to look up
   * @return the cached value, or null if not found
   */
  V get(K key);
}
