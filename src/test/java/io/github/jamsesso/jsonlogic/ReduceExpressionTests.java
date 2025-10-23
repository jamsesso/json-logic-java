package io.github.jamsesso.jsonlogic;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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

  @Test
  public void testReduceWVariableAccess() throws JsonLogicException {
    String json = "{\n" +
            "  \"reduce\": [\n" +
            "    {\n" +
            "      \"var\": \"data\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"*\": [\n" +
            "        {\n" +
            "          \"var\": \"multiplicator\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"+\": [\n" +
            "            {\n" +
            "              \"var\": \"current\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"var\": \"accumulator\"\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    0\n" +
            "  ]\n" +
            "}";
    final Map<String,Object> data = new HashMap<>();
    data.put("multiplicator", 2);
    data.put("data", new int[] {1, 2});
    Object result = jsonLogic.apply(json, data);

    assertEquals(8.0, result);
  }
}
