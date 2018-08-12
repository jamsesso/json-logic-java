package jamsesso.jsonlogic.evaluator;

import jamsesso.jsonlogic.JsonLogic;
import jamsesso.jsonlogic.JsonLogicException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MapExpressionTests {
  @Test
  public void testMap() throws JsonLogicException {
    String json = "{\"map\": [\n" +
                  "  {\"var\": \"\"},\n" +
                  "  {\"*\": [{\"var\": \"\"}, 2]}\n" +
                  "]}";
    int[] data = new int[] {1, 2, 3};
    Object result = JsonLogic.apply(json, data);

    assertEquals(3, ((List) result).size());
    assertEquals(2.0, ((List) result).get(0));
    assertEquals(4.0, ((List) result).get(1));
    assertEquals(6.0, ((List) result).get(2));
  }
}
