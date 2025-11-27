package io.github.jamsesso.jsonlogic;

import org.junit.Test;

import java.util.List;

import static io.github.jamsesso.jsonlogic.JsonLogicExceptionTestUtility.testErrorJsonPath;
import static org.junit.Assert.assertEquals;

public class MapExpressionTests {
  private static final JsonLogic jsonLogic = new JsonLogic();

  @Test
  public void testMap() throws JsonLogicException {
    String json = "{\"map\": [\n" +
                  "  {\"var\": \"\"},\n" +
                  "  {\"*\": [{\"var\": \"\"}, 2]}\n" +
                  "]}";
    int[] data = new int[] {1, 2, 3};
    Object result = jsonLogic.apply(json, data);

    assertEquals(3, ((List) result).size());
    assertEquals(2.0, ((List) result).get(0));
    assertEquals(4.0, ((List) result).get(1));
    assertEquals(6.0, ((List) result).get(2));
  }

  @Test
  public void testInvalidMap() {
    String json =  "{\"map\": [\n" +
        "  {\"var\": \"\"},\n" +
        "  {\"*\": [{}, 2]}\n" +
        // -------  ^  ---------
        "]}";

    String expectedErrorJsonPath = "$.map[1].*[0]";

    testErrorJsonPath(jsonLogic, json, expectedErrorJsonPath);
  }
}
