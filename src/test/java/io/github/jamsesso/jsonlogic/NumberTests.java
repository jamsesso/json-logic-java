package io.github.jamsesso.jsonlogic;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class NumberTests {
  @Test
  public void testConvertAllNumericInputToDouble() throws JsonLogicException {
    JsonLogic jsonLogic = new JsonLogic();
    Map<String, Number> numbers = new HashMap<String, Number>() {{
      put("double", 1D);
      put("float", 1F);
      put("int", 1);
      put("short", (short) 1);
      put("long", 1L);
    }};

    Assert.assertEquals("1.0", ((BigDecimal) jsonLogic.apply("{\"var\": \"double\"}", numbers)).toPlainString());
    Assert.assertEquals("1.0", ((BigDecimal) jsonLogic.apply("{\"var\": \"float\"}", numbers)).toPlainString());
    Assert.assertEquals("1", ((BigDecimal) jsonLogic.apply("{\"var\": \"int\"}", numbers)).toPlainString());
    Assert.assertEquals("1", ((BigDecimal) jsonLogic.apply("{\"var\": \"short\"}", numbers)).toPlainString());
    Assert.assertEquals("1", ((BigDecimal) jsonLogic.apply("{\"var\": \"long\"}", numbers)).toPlainString());
  }
}
