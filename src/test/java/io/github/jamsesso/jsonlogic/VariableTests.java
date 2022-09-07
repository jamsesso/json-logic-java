package io.github.jamsesso.jsonlogic;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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

    assertEquals(BigDecimal.valueOf(3.14), jsonLogic.apply("{\"var\": \"pi\"}", data));
  }

  @Test
  public void testDefaultValue() throws JsonLogicException {
    assertEquals(BigDecimal.valueOf(3.14), jsonLogic.apply("{\"var\": [\"pi\", 3.14]}", null));
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
    assertEquals(BigDecimal.valueOf(1337), jsonLogic.apply("{\"var\": \"users.0.followers\"}", data));
    assertEquals("Jane", jsonLogic.apply("{\"var\": \"users.1.name\"}", data));
    assertEquals(BigDecimal.valueOf(2048), jsonLogic.apply("{\"var\": \"users.1.followers\"}", data));
  }
}
