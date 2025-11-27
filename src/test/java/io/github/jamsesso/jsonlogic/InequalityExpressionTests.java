package io.github.jamsesso.jsonlogic;

import org.junit.Test;

import static io.github.jamsesso.jsonlogic.JsonLogicExceptionTestUtility.testErrorJsonPath;
import static org.junit.Assert.assertEquals;

public class InequalityExpressionTests {
  private static final JsonLogic jsonLogic = new JsonLogic();

  @Test
  public void testDifferentValueSameType() throws JsonLogicException {
    assertEquals(true, jsonLogic.apply("{\"!=\": [1, 2]}", null));
  }

  @Test
  public void testSameValueDifferentType() throws JsonLogicException {
    assertEquals(false, jsonLogic.apply("{\"!=\": [1.0, \"1\"]}", null));
  }

  @Test
  public void testInvalidArgumentCountInequality() {
    String json = "{\"!=\": [1]}";
    // ---------------------  ^  -
    String expectedErrorJsonPath = "$.!=";

    testErrorJsonPath(jsonLogic, json, expectedErrorJsonPath);
  }

  @Test
  public void testInvalidInequality() {
    String json = "{\"!=\": [{}, true]}";
    // ---------------------  ^  --------
    String expectedErrorJsonPath = "$.!=[0]";

    testErrorJsonPath(jsonLogic, json, expectedErrorJsonPath);
  }
}
