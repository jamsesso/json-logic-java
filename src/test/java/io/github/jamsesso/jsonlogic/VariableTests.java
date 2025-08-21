package io.github.jamsesso.jsonlogic;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class VariableTests {
  private static final JsonLogic jsonLogic = new JsonLogic();

  @Test
  public void testEmptyString() throws JsonLogicException {
    assertEquals(3.14, jsonLogic.apply("{\"var\": \"\"}", 3.14));
  }

  @Test
  public void testMapAccess() throws JsonLogicException {
    Map<String, Double> data = new HashMap<String, Double>() {{
      put("pi", 3.14);
    }};

    assertEquals(3.14, jsonLogic.apply("{\"var\": \"pi\"}", data));
  }

  @Test
  public void testDefaultValue() throws JsonLogicException {
    assertEquals(3.14, jsonLogic.apply("{\"var\": [\"pi\", 3.14]}", null));
  }

  @Test
  public void testUndefined() throws JsonLogicException {
    assertNull(jsonLogic.apply("{\"var\": [\"pi\"]}", null));
    assertNull(jsonLogic.apply("{\"var\": \"\"}", null));
    assertNull(jsonLogic.apply("{\"var\": 0}", null));
  }

  @Test
  public void testArrayAccess() throws JsonLogicException {
    String[] data = new String[] {"hello", "world"};

    assertEquals("hello", jsonLogic.apply("{\"var\": 0}", data));
    assertEquals("world", jsonLogic.apply("{\"var\": 1}", data));
    assertNull(jsonLogic.apply("{\"var\": 2}", data));
    assertNull(jsonLogic.apply("{\"var\": 3}", data));
  }

  @Test
  public void testArrayAccessWithStringKeys() throws JsonLogicException {
    String[] data = new String[] {"hello", "world"};

    assertEquals("hello", jsonLogic.apply("{\"var\": \"0\"}", data));
    assertEquals("world", jsonLogic.apply("{\"var\": \"1\"}", data));
    assertNull(jsonLogic.apply("{\"var\": \"2\"}", data));
    assertNull(jsonLogic.apply("{\"var\": \"3\"}", data));
  }

  @Test
  public void testListAccess() throws JsonLogicException {
    List<String> data = Arrays.asList("hello", "world");

    assertEquals("hello", jsonLogic.apply("{\"var\": 0}", data));
    assertEquals("world", jsonLogic.apply("{\"var\": 1}", data));
    assertNull(jsonLogic.apply("{\"var\": 2}", data));
    assertNull(jsonLogic.apply("{\"var\": 3}", data));
  }

  @Test
  public void testListAccessWithStringKeys() throws JsonLogicException {
    List<String> data = Arrays.asList("hello", "world");

    assertEquals("hello", jsonLogic.apply("{\"var\": \"0\"}", data));
    assertEquals("world", jsonLogic.apply("{\"var\": \"1\"}", data));
    assertNull(jsonLogic.apply("{\"var\": \"2\"}", data));
    assertNull(jsonLogic.apply("{\"var\": \"3\"}", data));
  }

  @Test
  public void testComplexAccess() throws JsonLogicException {
    Map<String, Object> data = new HashMap<String, Object>() {{
      put("users", Arrays.asList(
        new HashMap<String, Object>() {{
          put("name", "John");
          put("followers", 1337);
        }},
        new HashMap<String, Object>() {{
          put("name", "Jane");
          put("followers", 2048);
        }}
      ));
    }};

    assertEquals("John", jsonLogic.apply("{\"var\": \"users.0.name\"}", data));
    assertEquals(1337.0, jsonLogic.apply("{\"var\": \"users.0.followers\"}", data));
    assertEquals("Jane", jsonLogic.apply("{\"var\": \"users.1.name\"}", data));
    assertEquals(2048.0, jsonLogic.apply("{\"var\": \"users.1.followers\"}", data));
  }

  @Test
  public void missingNestedMapKey_returnsDefault() throws JsonLogicException {
    // data.a.b is missing -> should use default
    String rule = "{\"var\": [\"a.b.c\", \"fallback\"]}";
    Map<String, Object> data = map("a", map("b", new HashMap<>()));

    Object result = jsonLogic.apply(rule, data);

    assertEquals("fallback", result);
  }

  @Test
  public void presentNullLeaf_returnsNull_notDefault() throws JsonLogicException {
    // data.user.age present with value null -> should return null (no default)
    String rule = "{\"var\": [\"user.age\", 42]}";
    Map<String, Object> user = new HashMap<>();
    user.put("age", null);
    Map<String, Object> data = map("user", user);

    Object result = jsonLogic.apply(rule, data);

    assertNull(result);
  }

  @Test
  public void intermediateNull_returnsNull_notDefault() throws JsonLogicException {
    // data.a.b is null before finishing path -> should return null (no default)
    String rule = "{\"var\": [\"a.b.c\", \"fallback\"]}";
    Map<String, Object> data = map("a", map("b", null));

    Object result = jsonLogic.apply(rule, data);

    assertNull(result);
  }

  @Test
  public void nonTraversableIntermediate_returnsNull_notDefault() throws JsonLogicException {
    // data.a is a number; trying to access a.b -> should return null (no default)
    String rule = "{\"var\": [\"a.b\", \"fallback\"]}";
    Map<String, Object> data = map("a", 5);

    Object result = jsonLogic.apply(rule, data);

    assertNull(result);
  }

  @Test
  public void arrayIndexWithinBounds_returnsElement_asDoubleForNumbers() throws JsonLogicException {
    // items.1 exists -> should return 20 (as a double per evaluator.transform)
    String rule = "{\"var\": [\"items.1\", 999]}";
    Map<String, Object> data = map("items", Arrays.asList(10, 20));

    Object result = jsonLogic.apply(rule, data);

    assertTrue(result instanceof Number);
    assertEquals(20.0, ((Number) result).doubleValue(), 0.0);
  }

  @Test
  public void arrayIndexOutOfBounds_returnsDefault() throws JsonLogicException {
    // items.2 missing -> use default
    String rule = "{\"var\": [\"items.2\", \"missing\"]}";
    Map<String, Object> data = map("items", Arrays.asList(10, 20));

    Object result = jsonLogic.apply(rule, data);

    assertEquals("missing", result);
  }

  @Test
  public void arrayElementPresentButNull_returnsNull_notDefault() throws JsonLogicException {
    // items.0 exists and is null -> should return null (no default)
    String rule = "{\"var\": [\"items.0\", \"missing\"]}";
    Map<String, Object> data = map("items", Collections.singletonList(null));

    Object result = jsonLogic.apply(rule, data);

    assertNull(result);
  }

  @Test
  public void topLevelNumericIndex_overList_works() throws JsonLogicException {
    // {"var": [1, "missing"]} over a top-level list -> "banana"
    String rule = "{\"var\": [1, \"missing\"]}";
    List<String> data = Arrays.asList("apple", "banana", "carrot");

    Object result = jsonLogic.apply(rule, data);

    assertEquals("banana", result);
  }

  @Test
  public void emptyVarKey_returnsWholeDataObject() throws JsonLogicException {
    // {"var": ""} should return the entire data object (same instance)
    String rule = "{\"var\": \"\"}";
    Map<String, Object> data = map("x", 1);

    Object result = jsonLogic.apply(rule, data);

    assertSame("Should return the same data instance", data, result);
  }

  /** Helper to make small maps concisely. */
  private static Map<String, Object> map(Object... kv) {
    Map<String, Object> m = new HashMap<>();
    for (int i = 0; i < kv.length; i += 2) {
      m.put((String) kv[i], kv[i + 1]);
    }
    return m;
  }
}
