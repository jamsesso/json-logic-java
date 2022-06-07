package io.github.jamsesso.jsonlogic.cache;

import io.github.jamsesso.jsonlogic.ast.JsonLogicNode;

public interface JsonLogicCache {
    boolean containsKey(String key);

    void put(String key, JsonLogicNode value);

    JsonLogicNode get(String key);
}
