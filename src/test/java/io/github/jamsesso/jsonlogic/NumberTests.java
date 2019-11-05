package io.github.jamsesso.jsonlogic;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class NumberTests {
  @Test
  public void testConvertAllNumericInputToDouble() throws JsonLogicException {
    Map<String, Number> numbers = new HashMap<String, Number>() {{
      put("double", 1D);
      put("float", 1F);
      put("int", 1);
      put("short", (short) 1);
      put("long", 1L);
    }};

    JsonLogic jsonLogic = new JsonLogic();
    jsonLogic.addOperation("echo", args -> args[0]);

    Assert.assertEquals(1D, jsonLogic.apply("{\"echo\": [{\"var\": \"double\"}]}", numbers));
    Assert.assertEquals(1D, jsonLogic.apply("{\"echo\": [{\"var\": \"float\"}]}", numbers));
    Assert.assertEquals(1D, jsonLogic.apply("{\"echo\": [{\"var\": \"int\"}]}", numbers));
    Assert.assertEquals(1D, jsonLogic.apply("{\"echo\": [{\"var\": \"short\"}]}", numbers));
    Assert.assertEquals(1D, jsonLogic.apply("{\"echo\": [{\"var\": \"long\"}]}", numbers));
  }
}
