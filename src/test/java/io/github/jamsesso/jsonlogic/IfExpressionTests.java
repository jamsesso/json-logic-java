package io.github.jamsesso.jsonlogic;

import org.junit.Test;

import static io.github.jamsesso.jsonlogic.JsonLogicExceptionTestUtility.testErrorJsonPath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

  @Test
  public void testIfElseJsonPath_pos0() {
    String json = "{\"if\" : [{}, \"yes\", \"no\"]}";
    // ---------------------  ^  --------------------
    String expectedErrorJsonPath = "$.if[0]";

    testErrorJsonPath(jsonLogic, json, expectedErrorJsonPath);
  }

  @Test
  public void testIfElseJsonPath_pos1() {
    String json = "{\"if\" : [true, {}, \"no\"]}";
    // ---------------------------  ^  -----------
    String expectedErrorJsonPath = "$.if[1]";

    testErrorJsonPath(jsonLogic, json, expectedErrorJsonPath);
  }

  @Test
  public void testIfElseJsonPath_pos2() {
    String json = "{\"if\" : [false, \"yes\", {}]}";
    // -------------------------------------  ^  ---
    String expectedErrorJsonPath = "$.if[2]";

    testErrorJsonPath(jsonLogic, json, expectedErrorJsonPath);
  }
}
