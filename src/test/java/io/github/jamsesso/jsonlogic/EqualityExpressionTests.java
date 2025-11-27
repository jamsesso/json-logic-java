package io.github.jamsesso.jsonlogic;

import org.junit.Test;

import static io.github.jamsesso.jsonlogic.JsonLogicExceptionTestUtility.testErrorJsonPath;
import static org.junit.Assert.assertEquals;

public class EqualityExpressionTests {
  private static final JsonLogic jsonLogic = new JsonLogic();

  @Test
  public void testSameValueSameType() throws JsonLogicException {
    assertEquals(true, jsonLogic.apply("{\"==\": [1, 1]}", null));
  }

  @Test
  public void testSameValueDifferentType() throws JsonLogicException {
    assertEquals(true, jsonLogic.apply("{\"==\": [1, 1]}", null));
  }

  @Test
  public void testDifferentValueDifferentType() throws JsonLogicException {
    assertEquals(true, jsonLogic.apply("{\"==\": [[], false]}", null));
  }

  @Test
  public void testEmptyStringAndZeroComparison() throws JsonLogicException {
    assertEquals(true, jsonLogic.apply("{\"==\": [\" \", 0]}", null));
  }

  @Test
  public void testInvalidArgumentCountEquality() {
    String json = "{\"==\": [1]}";
    // ---------------------  ^  -
    String expectedErrorJsonPath = "$.==";

    testErrorJsonPath(jsonLogic, json, expectedErrorJsonPath);
  }

  @Test
  public void testInvalidEquality() {
    String json = "{\"==\": [{}, true]}";
    // ---------------------  ^  --------
    String expectedErrorJsonPath = "$.==[0]";

    testErrorJsonPath(jsonLogic, json, expectedErrorJsonPath);
  }
}
