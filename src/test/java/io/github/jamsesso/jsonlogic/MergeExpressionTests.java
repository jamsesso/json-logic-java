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
    assertEquals(0, result.get(0).compareTo(new BigDecimal("1.0")));
    assertEquals(0, result.get(1).compareTo(new BigDecimal("2.0")));
    assertEquals(0, result.get(2).compareTo(new BigDecimal("3.0")));
    assertEquals(0, result.get(3).compareTo(new BigDecimal("4.0")));
  }

  @Test
  public void testMergeWithNonArrays() throws JsonLogicException {
    List<BigDecimal> result = (List) jsonLogic.apply("{\"merge\": [1, 2, [3, 4]]}", null);

    assertEquals(4, result.size());
    assertEquals(0, result.get(0).compareTo(new BigDecimal("1.0")));
    assertEquals(0, result.get(1).compareTo(new BigDecimal("2.0")));
    assertEquals(0, result.get(2).compareTo(new BigDecimal("3.0")));
    assertEquals(0, result.get(3).compareTo(new BigDecimal("4.0")));
  }
}
