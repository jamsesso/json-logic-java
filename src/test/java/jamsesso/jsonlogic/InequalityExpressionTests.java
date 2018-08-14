package jamsesso.jsonlogic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InequalityExpressionTests {
  private static final JsonLogic jsonLogic = new JsonLogic();

  @Test
  public void testDifferentValueSameType() throws JsonLogicException {
    assertEquals(true, jsonLogic.apply("{\"!=\": [1, 2]}", null));
  }

  @Test
  public void testSameValueDifferentType() throws JsonLogicException {
    assertEquals(false, jsonLogic.apply("{\"!=\": [1.0, \"1\"]}", null));
  }
}
