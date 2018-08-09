package jamsesso.jsonlogic.evaluator;

import jamsesso.jsonlogic.JsonLogic;
import jamsesso.jsonlogic.JsonLogicException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumericComparisonExpressionTests {
  @Test
  public void testLessThan() throws JsonLogicException {
    String json = "{\"<\" : [1, 2]}";
    Object result = JsonLogic.apply(json, null);

    assertEquals(true, result);
  }

  @Test
  public void testLessThanOrEqual() throws JsonLogicException {
    String json = "{\"<=\" : [1, 1]}";
    Object result = JsonLogic.apply(json, null);

    assertEquals(true, result);
  }

  @Test
  public void testGreaterThan() throws JsonLogicException {
    String json = "{\">\" : [2, 1]}";
    Object result = JsonLogic.apply(json, null);

    assertEquals(true, result);
  }

  @Test
  public void testGreaterThanOrEqual() throws JsonLogicException {
    String json = "{\">=\" : [1, 1]}";
    Object result = JsonLogic.apply(json, null);

    assertEquals(true, result);
  }

  @Test
  public void testBetweenExclusive() throws JsonLogicException {
    String json = "{\"<\" : [1, 2, 3]}";
    Object result = JsonLogic.apply(json, null);

    assertEquals(true, result);
  }

  @Test
  public void testBetweenInclusive() throws JsonLogicException {
    String json = "{\"<=\" : [1, 1, 3]}";
    Object result = JsonLogic.apply(json, null);

    assertEquals(true, result);
  }
}
