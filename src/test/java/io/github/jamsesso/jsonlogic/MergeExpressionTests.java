package io.github.jamsesso.jsonlogic;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MergeExpressionTests {
  private static final JsonLogic jsonLogic = new JsonLogic();

  @Test
  public void testMerge() throws JsonLogicException {
    List<BigDecimal> result = (List) jsonLogic.apply("{\"merge\": [[1, 2], [3, 4]]}", null);

    assertEquals(4, result.size());
    assertEquals("1", result.get(0).toPlainString());
    assertEquals("2", result.get(1).toPlainString());
    assertEquals("3", result.get(2).toPlainString());
    assertEquals("4", result.get(3).toPlainString());
  }

  @Test
  public void testMergeWithNonArrays() throws JsonLogicException {
    List<BigDecimal> result = (List) jsonLogic.apply("{\"merge\": [1, 2, [3, 4]]}", null);

    assertEquals(4, result.size());
    assertEquals("1", result.get(0).toPlainString());
    assertEquals("2", result.get(1).toPlainString());
    assertEquals("3", result.get(2).toPlainString());
    assertEquals("4", result.get(3).toPlainString());
  }
}
