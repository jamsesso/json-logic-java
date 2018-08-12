package jamsesso.jsonlogic.evaluator;

import jamsesso.jsonlogic.JsonLogic;
import jamsesso.jsonlogic.JsonLogicException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InExpressionTests {
  @Test
  public void testStringIn() throws JsonLogicException {
    assertEquals(true, JsonLogic.apply("{\"in\": [\"race\", \"racecar\"]}", null));
  }

  @Test
  public void testStringNotIn() throws JsonLogicException {
    assertEquals(false, JsonLogic.apply("{\"in\": [\"race\", \"clouds\"]}", null));
  }

  @Test
  public void testArrayIn() throws JsonLogicException {
    assertEquals(true, JsonLogic.apply("{\"in\": [1, [1, 2, 3]]}", null));
    assertEquals(true, JsonLogic.apply("{\"in\": [4.56, [1, 2, 3, 4.56]]}", null));
  }

  @Test
  public void testArrayNotIn() throws JsonLogicException {
    assertEquals(false, JsonLogic.apply("{\"in\": [5, [1, 2, 3]]}", null));
  }
}
