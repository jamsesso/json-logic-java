package io.github.jamsesso.jsonlogic.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentHashMapCacheManager is a cache manager that uses ConcurrentHashMap for caching.
 * It provides methods to check if a key exists, put a value in the cache,
 * and retrieve a value from the cache.
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of cached values
 */
public class ConcurrentHashMapCacheManager<K, V> implements CacheManager<K, V> {
    private final ConcurrentHashMap<K, V> cache;

    public ConcurrentHashMapCacheManager() {
        this.cache = new ConcurrentHashMap<>();
    }

    @Override
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }
    
}
