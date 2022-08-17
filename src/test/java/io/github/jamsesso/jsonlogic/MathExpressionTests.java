package io.github.jamsesso.jsonlogic;

import org.junit.Test;

import java.math.BigDecimal;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MathExpressionTests {
  private static final JsonLogic jsonLogic = new JsonLogic();

  @Test
  public void testAdd() throws JsonLogicException {
    String json = "{\"+\":[4,2]}";
    Object result = jsonLogic.apply(json, null);

    assertEquals(0, ((BigDecimal) result).compareTo(BigDecimal.valueOf(6)));
  }

  @Test
  public void testMultiAdd() throws JsonLogicException {
    String json = "{\"+\":[2,2,2,2,2]}";
    Object result = jsonLogic.apply(json, null);

    assertEquals(0, ((BigDecimal) result).compareTo(BigDecimal.valueOf(10)));
  }

  @Test
  public void testSingleAdd() throws JsonLogicException {
    String json = "{\"+\" : \"3.14\"}";
    Object result = jsonLogic.apply(json, null);

    assertEquals(0, ((BigDecimal) result).compareTo(BigDecimal.valueOf(3.14)));
  }

  @Test
  public void testSubtract() throws JsonLogicException {
    String json = "{\"-\":[4,2]}";
    Object result = jsonLogic.apply(json, null);

    assertEquals(0, ((BigDecimal) result).compareTo(BigDecimal.valueOf(2)));
  }

  @Test
  public void testSingleSubtract() throws JsonLogicException {
    String json = "{\"-\": 2 }";
    Object result = jsonLogic.apply(json, null);

    assertEquals(0, ((BigDecimal) result).compareTo(BigDecimal.valueOf(-2)));
  }

  @Test
  public void testMultiply() throws JsonLogicException {
    String json = "{\"*\":[4,2]}";
    Object result = jsonLogic.apply(json, null);

    assertEquals(0, ((BigDecimal) result).compareTo(BigDecimal.valueOf(8)));
  }

  @Test
  public void testMultiMultiply() throws JsonLogicException {
    String json = "{\"*\":[2,2,2,2,2]}";
    Object result = jsonLogic.apply(json, null);

    assertEquals(0, ((BigDecimal) result).compareTo(BigDecimal.valueOf(32)));
  }

  @Test
  public void testDivide() throws JsonLogicException {
    String json = "{\"/\":[4,2]}";
    Object result = jsonLogic.apply(json, null);


    assertEquals(0, ((BigDecimal) result).compareTo(BigDecimal.valueOf(2)));
  }

  @Test(expected = ArithmeticException.class)
  public void testDivideBy0() throws JsonLogicException {
    String json = "{\"/\":[4,0]}";
    jsonLogic.apply(json, null);
  }

  @Test
  public void testModulo() throws JsonLogicException {
    String json = "{\"%\": [101,2]}";
    Object result = jsonLogic.apply(json, null);

    assertEquals(0, ((BigDecimal) result).compareTo(BigDecimal.valueOf(1)));
  }

  @Test
  public void testMin() throws JsonLogicException {
    String json = "{\"min\":[1,2,3]}";
    Object result = jsonLogic.apply(json, null);

    assertEquals(0, ((BigDecimal) result).compareTo(BigDecimal.valueOf(1)));
  }

  @Test
  public void testMax() throws JsonLogicException {
    String json = "{\"max\":[1,2,3]}";
    Object result = jsonLogic.apply(json, null);

    assertEquals(0, ((BigDecimal) result).compareTo(BigDecimal.valueOf(3)));
  }

  @Test
  public void testDivideSingleNumber() throws JsonLogicException {
    assertNull(jsonLogic.apply("{\"/\": [0]}", null));
  }
}
