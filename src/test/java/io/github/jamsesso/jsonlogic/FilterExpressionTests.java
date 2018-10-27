package io.github.jamsesso.jsonlogic;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class FilterExpressionTests {
  private static final JsonLogic jsonLogic = new JsonLogic();

  @Test
  public void testMap() throws JsonLogicException {
    String json = "{\"filter\": [\n" +
                  "  {\"var\": \"\"},\n" +
                  "  {\"==\": [{\"%\": [{\"var\": \"\"}, 2]}, 0]}\n" +
                  "]}";
    int[] data = new int[] {1, 2, 3, 4, 5, 6};
    Object result = jsonLogic.apply(json, data);

    assertEquals(3, ((List) result).size());
    assertEquals(2, ((List) result).get(0));
    assertEquals(4, ((List) result).get(1));
    assertEquals(6, ((List) result).get(2));
  }
}
