package io.github.jamsesso.jsonlogic;

import io.github.jamsesso.jsonlogic.ast.JsonLogicNode;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.*;

public class JsonLogicCacheTests {
  @Test
  public void reusesParsedRulesUntilEvictedByCaller() throws JsonLogicException {
    ConcurrentMap<String, JsonLogicNode> cache = new ConcurrentHashMap<>();
    JsonLogic logic = JsonLogic.withParseCache(cache);
    String rule = "{\"var\":\"\"}";

    assertEquals("first", logic.apply(rule, "first"));
    JsonLogicNode parsed = cache.get(rule);
    assertNotNull(parsed);
    assertEquals("second", logic.apply(rule, "second"));
    assertSame(parsed, cache.remove(rule));

    assertEquals("third", logic.apply(rule, "third"));
    assertNotNull(cache.get(rule));
    assertNotSame(parsed, cache.get(rule));
  }

  @Test
  public void evaluatesEvenIfCacheEvictsImmediately() throws JsonLogicException {
    ConcurrentMap<String, JsonLogicNode> cache = new ConcurrentHashMap<String, JsonLogicNode>() {
      @Override
      public JsonLogicNode putIfAbsent(String key, JsonLogicNode rule) {
        JsonLogicNode existing = super.putIfAbsent(key, rule);
        remove(key);
        return existing;
      }
    };

    assertEquals(true, JsonLogic.withParseCache(cache).apply("true", null));
    assertTrue(cache.isEmpty());
  }

  @Test(expected = NullPointerException.class)
  public void rejectsNullCache() {
    JsonLogic.withParseCache(null);
  }
}
