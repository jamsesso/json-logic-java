package jamsesso.jsonlogic.evaluator;

import jamsesso.jsonlogic.JsonLogic;
import jamsesso.jsonlogic.JsonLogicException;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class VariableTests {
  @Test
  public void testEmptyString() throws JsonLogicException {
    assertEquals(3.14, JsonLogic.apply("{\"var\": \"\"}", 3.14));
  }

  @Test
  public void testMapAccess() throws JsonLogicException {
    Map<String, Double> data = new HashMap<String, Double>() {{
      put("pi", 3.14);
    }};

    assertEquals(3.14, JsonLogic.apply("{\"var\": \"pi\"}", data));
  }

  @Test
  public void testDefaultValue() throws JsonLogicException {
    assertEquals(3.14, JsonLogic.apply("{\"var\": [\"pi\", 3.14]}", null));
  }

  @Test
  public void testUndefined() throws JsonLogicException {
    assertNull(JsonLogic.apply("{\"var\": [\"pi\"]}", null));
    assertNull(JsonLogic.apply("{\"var\": \"\"}", null));
    assertNull(JsonLogic.apply("{\"var\": 0}", null));
  }

  @Test
  public void testArrayAccess() throws JsonLogicException {
    String[] data = new String[] {"hello", "world"};

    assertEquals("hello", JsonLogic.apply("{\"var\": 0}", data));
    assertEquals("world", JsonLogic.apply("{\"var\": 1}", data));
  }

  @Test
  public void testListAccess() throws JsonLogicException {
    List<String> data = Arrays.asList("hello", "world");

    assertEquals("hello", JsonLogic.apply("{\"var\": 0}", data));
    assertEquals("world", JsonLogic.apply("{\"var\": 1}", data));
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

    assertEquals("John", JsonLogic.apply("{\"var\": \"users.0.name\"}", data));
    assertEquals(1337, JsonLogic.apply("{\"var\": \"users.0.followers\"}", data));
    assertEquals("Jane", JsonLogic.apply("{\"var\": \"users.1.name\"}", data));
    assertEquals(2048, JsonLogic.apply("{\"var\": \"users.1.followers\"}", data));
  }
}
