package io.github.jamsesso.jsonlogic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArrayHasExpressionTests {
  private static final JsonLogic jsonLogic = new JsonLogic();

  @Test
  public void testSomeWithNull() throws JsonLogicException {
    assertEquals(false, jsonLogic.apply("{\"and\":[{\"some\":[{\"var\":\"fruits\"},{\"in\":[{\"var\":\"\"},[\"apple\"]]}]}]}", "{\"fruits\":null}"));
  }

  @Test
  public void testSomeEmptyArray() throws JsonLogicException {
    assertEquals(false, jsonLogic.apply("{\"some\": [[], {\">\": [{\"var\": \"\"}, 0]}]}", null));
  }

  @Test
  public void testSomeAll() throws JsonLogicException {
    assertEquals(false, jsonLogic.apply("{\"some\": [[1, 2, 3], {\">\": [{\"var\": \"\"}, 3]}]}", null));
    assertEquals(true, jsonLogic.apply("{\"some\": [[1, 2, 3], {\">\": [{\"var\": \"\"}, 1]}]}", null));
  }

  @Test
  public void testNoneWithNull() throws JsonLogicException {
    assertEquals(true, jsonLogic.apply("{\"and\":[{\"none\":[{\"var\":\"fruits\"},{\"in\":[{\"var\":\"\"},[\"apple\"]]}]}]}", "{\"fruits\":null}"));
  }

  @Test
  public void testNoneEmptyArray() throws JsonLogicException {
    assertEquals(false, jsonLogic.apply("{\"some\": [[], {\">\": [{\"var\": \"\"}, 0]}]}", null));
  }

  @Test
  public void testNoneAll() throws JsonLogicException {
    assertEquals(true, jsonLogic.apply("{\"none\": [[1, 2, 3], {\">\": [{\"var\": \"\"}, 3]}]}", null));
    assertEquals(false, jsonLogic.apply("{\"none\": [[1, 2, 3], {\">\": [{\"var\": \"\"}, 2]}]}", null));
  }
}