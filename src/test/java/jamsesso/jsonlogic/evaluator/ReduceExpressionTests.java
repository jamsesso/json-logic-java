package jamsesso.jsonlogic.evaluator;

import jamsesso.jsonlogic.JsonLogic;
import jamsesso.jsonlogic.JsonLogicException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReduceExpressionTests {
  @Test
  public void testReduce() throws JsonLogicException {
    String json = "{\"reduce\":[\n" +
                  "    {\"var\":\"\"},\n" +
                  "    {\"+\":[{\"var\":\"current\"}, {\"var\":\"accumulator\"}]},\n" +
                  "    0\n" +
                  "]}";
    int[] data = new int[] {1, 2, 3, 4, 5, 6};
    Object result = JsonLogic.apply(json, data);

    assertEquals(21.0, result);
  }
}
