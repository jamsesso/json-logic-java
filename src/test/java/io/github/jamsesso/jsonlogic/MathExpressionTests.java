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
    BigDecimal result = (BigDecimal) jsonLogic.apply(json, null);

    assertEquals("6", result.toPlainString());
  }

  @Test
  public void testMultiAdd() throws JsonLogicException {
    String json = "{\"+\":[2,2,2,2,2]}";
    BigDecimal result = (BigDecimal) jsonLogic.apply(json, null);

    assertEquals("10", result.toPlainString());
  }

  @Test
  public void testSingleAdd() throws JsonLogicException {
    String json = "{\"+\" : \"3.14\"}";
    BigDecimal result = (BigDecimal) jsonLogic.apply(json, null);

    assertEquals("3.14", result.toPlainString());
  }

  @Test
  public void testSubtract() throws JsonLogicException {
    String json = "{\"-\":[4,2]}";
    BigDecimal result = (BigDecimal) jsonLogic.apply(json, null);

    assertEquals("2", result.toPlainString());
  }

  @Test
  public void testSingleSubtract() throws JsonLogicException {
    String json = "{\"-\": 2 }";
    BigDecimal result = (BigDecimal) jsonLogic.apply(json, null);

    assertEquals("-2", result.toPlainString());
  }

  @Test
  public void testMultiply() throws JsonLogicException {
    String json = "{\"*\":[4,2]}";
    BigDecimal result = (BigDecimal) jsonLogic.apply(json, null);

    assertEquals("8", result.toPlainString());
  }

  @Test
  public void testMultiMultiply() throws JsonLogicException {
    String json = "{\"*\":[2,2,2,2,2]}";
    BigDecimal result = (BigDecimal) jsonLogic.apply(json, null);

    assertEquals("32", result.toPlainString());
  }

  @Test
  public void testDivide() throws JsonLogicException {
    String json = "{\"/\":[4,2]}";
    BigDecimal result = (BigDecimal) jsonLogic.apply(json, null);


    assertEquals("2", result.stripTrailingZeros().toPlainString());
  }

  @Test(expected = ArithmeticException.class)
  public void testDivideBy0() throws JsonLogicException {
    String json = "{\"/\":[4,0]}";
    jsonLogic.apply(json, null);
  }

  @Test
  public void testModulo() throws JsonLogicException {
    String json = "{\"%\": [101,2]}";
    BigDecimal result = (BigDecimal) jsonLogic.apply(json, null);

    assertEquals("1", result.toPlainString());
  }

  @Test
  public void testMin() throws JsonLogicException {
    String json = "{\"min\":[1,2,3]}";
    BigDecimal result = (BigDecimal) jsonLogic.apply(json, null);

    assertEquals("1", result.toPlainString());
  }

  @Test
  public void testMax() throws JsonLogicException {
    String json = "{\"max\":[1,2,3]}";
    BigDecimal result = (BigDecimal) jsonLogic.apply(json, null);

    assertEquals("3", result.toPlainString());
  }

  @Test
  public void testDivideSingleNumber() throws JsonLogicException {
    assertNull(jsonLogic.apply("{\"/\": [0]}", null));
  }
}
