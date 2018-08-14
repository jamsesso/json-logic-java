package jamsesso.jsonlogic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LogExpressionTests {
  private static final JsonLogic jsonLogic = new JsonLogic();

  @Test
  public void testDoesLog() throws JsonLogicException {
    assertEquals("hello world", jsonLogic.apply("{\"log\": \"hello world\"}", null));
  }
}
