package jamsesso.jsonlogic.evaluator;

import jamsesso.jsonlogic.JsonLogic;
import jamsesso.jsonlogic.JsonLogicException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SubstringExpressionTests {
  @Test
  public void testSubstringSingleArg() throws JsonLogicException {
    assertEquals("logic", JsonLogic.apply("{\"substr\": [\"jsonlogic\", 4]}", null));
  }

  @Test
  public void testSubstringSingleNegativeArg() throws JsonLogicException {
    assertEquals("logic", JsonLogic.apply("{\"substr\": [\"jsonlogic\", -5]}", null));
  }

  @Test
  public void testSubstringDoubleArg() throws JsonLogicException {
    assertEquals("son", JsonLogic.apply("{\"substr\": [\"jsonlogic\", 1, 3]}", null));
  }

  @Test
  public void testSubstringDoubleNegativeArg() throws JsonLogicException {
    assertEquals("log", JsonLogic.apply("{\"substr\": [\"jsonlogic\", 4, -2]}", null));
  }

  @Test
  public void testSubstringSingleArgOutOfBounds() throws JsonLogicException {
    assertEquals("", JsonLogic.apply("{\"substr\": [\"jsonlogic\", -40]}", null));
  }

  @Test
  public void testSubstringDoubleArgOutOfBounds() throws JsonLogicException {
    assertEquals("", JsonLogic.apply("{\"substr\": [\"jsonlogic\", 20, -40]}", null));
  }
}
