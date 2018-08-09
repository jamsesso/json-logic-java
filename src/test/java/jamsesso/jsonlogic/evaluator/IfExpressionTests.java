package jamsesso.jsonlogic.evaluator;

import jamsesso.jsonlogic.JsonLogic;
import jamsesso.jsonlogic.JsonLogicException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IfExpressionTests {
  @Test
  public void testIfTrue() throws JsonLogicException {
    String json = "{\"if\" : [true, \"yes\", \"no\"]}";
    Object result = JsonLogic.apply(json, null);

    assertEquals("yes", result);
  }

  @Test
  public void testIfFalse() throws JsonLogicException {
    String json = "{\"if\" : [false, \"yes\", \"no\"]}";
    Object result = JsonLogic.apply(json, null);

    assertEquals("no", result);
  }

  @Test
  public void testIfElseIfElse() throws JsonLogicException {
    String json = "{\"if\" : [\n" +
                  "  {\"<\": [50, 0]}, \"freezing\",\n" +
                  "  {\"<\": [50, 100]}, \"liquid\",\n" +
                  "  \"gas\"\n" +
                  "]}";
    Object result = JsonLogic.apply(json, null);

    assertEquals("liquid", result);
  }
}
