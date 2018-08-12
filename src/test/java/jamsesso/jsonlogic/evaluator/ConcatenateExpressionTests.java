package jamsesso.jsonlogic.evaluator;

import jamsesso.jsonlogic.JsonLogic;
import jamsesso.jsonlogic.JsonLogicException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConcatenateExpressionTests {
  @Test
  public void testCat() throws JsonLogicException {
    assertEquals("hello world 2!", JsonLogic.apply("{\"cat\": [\"hello\", \" world \", 2, \"!\"]}", null));
    assertEquals("pi is 3.14159", JsonLogic.apply("{\"cat\": [\"pi is \", 3.14159]}", null));
  }
}
