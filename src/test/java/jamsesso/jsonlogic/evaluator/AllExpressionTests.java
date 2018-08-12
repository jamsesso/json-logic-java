package jamsesso.jsonlogic.evaluator;

import jamsesso.jsonlogic.JsonLogic;
import jamsesso.jsonlogic.JsonLogicException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AllExpressionTests {
  @Test
  public void testEmptyArray() throws JsonLogicException {
    assertEquals(true, JsonLogic.apply("{\"all\": [[], {\">\": [{\"var\": \"\"}, 0]}]}", null));
  }

  @Test
  public void testAll() throws JsonLogicException {
    assertEquals(true, JsonLogic.apply("{\"all\": [[1, 2, 3], {\">\": [{\"var\": \"\"}, 0]}]}", null));
    assertEquals(false, JsonLogic.apply("{\"all\": [[1, 2, 3], {\">\": [{\"var\": \"\"}, 1]}]}", null));
  }
}
