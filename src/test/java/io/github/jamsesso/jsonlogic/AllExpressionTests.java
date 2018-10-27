package io.github.jamsesso.jsonlogic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AllExpressionTests {
  private static final JsonLogic jsonLogic = new JsonLogic();

  @Test
  public void testEmptyArray() throws JsonLogicException {
    assertEquals(false, jsonLogic.apply("{\"all\": [[], {\">\": [{\"var\": \"\"}, 0]}]}", null));
  }

  @Test
  public void testAll() throws JsonLogicException {
    assertEquals(true, jsonLogic.apply("{\"all\": [[1, 2, 3], {\">\": [{\"var\": \"\"}, 0]}]}", null));
    assertEquals(false, jsonLogic.apply("{\"all\": [[1, 2, 3], {\">\": [{\"var\": \"\"}, 1]}]}", null));
  }
}
