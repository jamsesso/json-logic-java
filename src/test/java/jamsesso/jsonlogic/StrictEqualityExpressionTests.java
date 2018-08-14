package jamsesso.jsonlogic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StrictEqualityExpressionTests {
  private static final JsonLogic jsonLogic = new JsonLogic();

  @Test
  public void testSameValueSameType() throws JsonLogicException {
    assertEquals(true, jsonLogic.apply("{\"===\": [1, 1.0]}", null));
  }

  @Test
  public void testSameValueDifferentType() throws JsonLogicException {
    assertEquals(false, jsonLogic.apply("{\"===\": [1, \"1\"]}", null));
  }
}
