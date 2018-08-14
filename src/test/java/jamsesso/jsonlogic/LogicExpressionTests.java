package jamsesso.jsonlogic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LogicExpressionTests {
  private static final JsonLogic jsonLogic = new JsonLogic();

  @Test
  public void testOr() throws JsonLogicException {
    assertEquals("a", jsonLogic.apply("{\"or\": [0, false, \"a\"]}", null));
  }

  @Test
  public void testAnd() throws JsonLogicException {
    assertEquals("", jsonLogic.apply("{\"and\": [true, \"\", 3]}", null));
  }
}
