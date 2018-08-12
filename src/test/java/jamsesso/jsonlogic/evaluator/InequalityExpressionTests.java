package jamsesso.jsonlogic.evaluator;

import jamsesso.jsonlogic.JsonLogic;
import jamsesso.jsonlogic.JsonLogicException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InequalityExpressionTests {
  @Test
  public void testDifferentValueSameType() throws JsonLogicException {
    assertEquals(true, JsonLogic.apply("{\"!=\": [1, 2]}", null));
  }

  @Test
  public void testSameValueDifferentType() throws JsonLogicException {
    assertEquals(false, JsonLogic.apply("{\"!=\": [1.0, \"1\"]}", null));
  }
}
