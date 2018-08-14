package jamsesso.jsonlogic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IfExpressionTests {
  private static final JsonLogic jsonLogic = new JsonLogic();

  @Test
  public void testIfTrue() throws JsonLogicException {
    String json = "{\"if\" : [true, \"yes\", \"no\"]}";
    Object result = jsonLogic.apply(json, null);

    assertEquals("yes", result);
  }

  @Test
  public void testIfFalse() throws JsonLogicException {
    String json = "{\"if\" : [false, \"yes\", \"no\"]}";
    Object result = jsonLogic.apply(json, null);

    assertEquals("no", result);
  }

  @Test
  public void testIfElseIfElse() throws JsonLogicException {
    String json = "{\"if\" : [\n" +
                  "  {\"<\": [50, 0]}, \"freezing\",\n" +
                  "  {\"<\": [50, 100]}, \"liquid\",\n" +
                  "  \"gas\"\n" +
                  "]}";
    Object result = jsonLogic.apply(json, null);

    assertEquals("liquid", result);
  }
}
