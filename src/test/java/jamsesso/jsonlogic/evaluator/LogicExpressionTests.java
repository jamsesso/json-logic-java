package jamsesso.jsonlogic.evaluator;

import jamsesso.jsonlogic.JsonLogic;
import jamsesso.jsonlogic.JsonLogicException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LogicExpressionTests {
  @Test
  public void testOr() throws JsonLogicException {
    assertEquals("a", JsonLogic.apply("{\"or\": [0, false, \"a\"]}", null));
  }

  @Test
  public void testAnd() throws JsonLogicException {
    assertEquals("", JsonLogic.apply("{\"and\": [true, \"\", 3]}", null));
  }
}
