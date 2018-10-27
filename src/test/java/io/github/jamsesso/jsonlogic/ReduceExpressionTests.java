package io.github.jamsesso.jsonlogic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReduceExpressionTests {
  private static final JsonLogic jsonLogic = new JsonLogic();

  @Test
  public void testReduce() throws JsonLogicException {
    String json = "{\"reduce\":[\n" +
                  "    {\"var\":\"\"},\n" +
                  "    {\"+\":[{\"var\":\"current\"}, {\"var\":\"accumulator\"}]},\n" +
                  "    0\n" +
                  "]}";
    int[] data = new int[] {1, 2, 3, 4, 5, 6};
    Object result = jsonLogic.apply(json, data);

    assertEquals(21.0, result);
  }
}
