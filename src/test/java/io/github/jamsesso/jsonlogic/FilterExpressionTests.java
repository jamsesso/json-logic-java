package io.github.jamsesso.jsonlogic;

import org.junit.Test;

import java.math.BigDecimal;
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
    List<BigDecimal> result = (List) jsonLogic.apply(json, data);

    assertEquals(3, result.size());
    assertEquals("2", result.get(0).toPlainString());
    assertEquals("4", result.get(1).toPlainString());
    assertEquals("6", result.get(2).toPlainString());
  }
}
