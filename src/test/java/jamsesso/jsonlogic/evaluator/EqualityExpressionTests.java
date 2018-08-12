package jamsesso.jsonlogic.evaluator;

import jamsesso.jsonlogic.JsonLogic;
import jamsesso.jsonlogic.JsonLogicException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EqualityExpressionTests {
  @Test
  public void testSameValueSameType() throws JsonLogicException {
    assertEquals(true, JsonLogic.apply("{\"==\": [1, 1]}", null));
  }

  @Test
  public void testSameValueDifferentType() throws JsonLogicException {
    assertEquals(true, JsonLogic.apply("{\"==\": [1, 1]}", null));
  }

  @Test
  public void testDifferentValueDifferentType() throws JsonLogicException {
    assertEquals(true, JsonLogic.apply("{\"==\": [[], false]}", null));
  }
}
