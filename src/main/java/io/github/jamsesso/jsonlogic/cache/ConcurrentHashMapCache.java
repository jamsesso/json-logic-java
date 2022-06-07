package io.github.jamsesso.jsonlogic.cache;

import io.github.jamsesso.jsonlogic.ast.JsonLogicNode;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapCache implements JsonLogicCache {

    private final ConcurrentHashMap<String, JsonLogicNode> parseCache = new ConcurrentHashMap<>();

    @Override
    public boolean containsKey(String key) {
        return parseCache.containsKey(key);
    }

    @Override
    public void put(String key, JsonLogicNode value) {
        parseCache.put(key, value);
    }

    @Override
    public JsonLogicNode get(String key) {
        return parseCache.get(key);
    }
}
