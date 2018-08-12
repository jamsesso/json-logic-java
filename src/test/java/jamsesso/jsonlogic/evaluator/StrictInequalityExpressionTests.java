package jamsesso.jsonlogic.evaluator;

import jamsesso.jsonlogic.JsonLogic;
import jamsesso.jsonlogic.JsonLogicException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StrictInequalityExpressionTests {
  @Test
  public void testSameValueSameType() throws JsonLogicException {
    assertEquals(false, JsonLogic.apply("{\"!==\": [1, 1.0]}", null));
  }

  @Test
  public void testSameValueDifferentType() throws JsonLogicException {
    assertEquals(true, JsonLogic.apply("{\"!==\": [1, \"1\"]}", null));
  }
}
