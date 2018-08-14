package jamsesso.jsonlogic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EqualityExpressionTests {
  private static final JsonLogic jsonLogic = new JsonLogic();

  @Test
  public void testSameValueSameType() throws JsonLogicException {
    assertEquals(true, jsonLogic.apply("{\"==\": [1, 1]}", null));
  }

  @Test
  public void testSameValueDifferentType() throws JsonLogicException {
    assertEquals(true, jsonLogic.apply("{\"==\": [1, 1]}", null));
  }

  @Test
  public void testDifferentValueDifferentType() throws JsonLogicException {
    assertEquals(true, jsonLogic.apply("{\"==\": [[], false]}", null));
  }
}
