package jamsesso.jsonlogic.evaluator;

import jamsesso.jsonlogic.JsonLogic;
import jamsesso.jsonlogic.JsonLogicException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LogExpressionTests {
  @Test
  public void testDoesLog() throws JsonLogicException {
    assertEquals("hello world", JsonLogic.apply("{\"log\": \"hello world\"}", null));
  }
}
