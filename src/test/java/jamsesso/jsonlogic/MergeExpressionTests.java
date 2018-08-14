package jamsesso.jsonlogic;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MergeExpressionTests {
  private static final JsonLogic jsonLogic = new JsonLogic();

  @Test
  public void testMerge() throws JsonLogicException {
    Object result = jsonLogic.apply("{\"merge\": [[1, 2], [3, 4]]}", null);

    assertEquals(4, ((List) result).size());
    assertEquals(1.0, ((List) result).get(0));
    assertEquals(2.0, ((List) result).get(1));
    assertEquals(3.0, ((List) result).get(2));
    assertEquals(4.0, ((List) result).get(3));
  }

  @Test
  public void testMergeWithNonArrays() throws JsonLogicException {
    Object result = jsonLogic.apply("{\"merge\": [1, 2, [3, 4]]}", null);

    assertEquals(4, ((List) result).size());
    assertEquals(1.0, ((List) result).get(0));
    assertEquals(2.0, ((List) result).get(1));
    assertEquals(3.0, ((List) result).get(2));
    assertEquals(4.0, ((List) result).get(3));
  }
}
