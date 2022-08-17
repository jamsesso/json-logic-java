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
    BigDecimal[] data = new BigDecimal[]{ BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(3),
            BigDecimal.valueOf(4),BigDecimal.valueOf(5),BigDecimal.valueOf(6)};
    List<BigDecimal> result = (List) jsonLogic.apply(json, data);

    assertEquals(3, result.size());
    assertEquals(0, result.get(0).compareTo(new BigDecimal("2.0")));
    assertEquals(0, result.get(1).compareTo(new BigDecimal("4.0")));
    assertEquals(0, result.get(2).compareTo(new BigDecimal("6.0")));
  }
}
