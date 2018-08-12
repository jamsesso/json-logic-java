package jamsesso.jsonlogic.evaluator;

import jamsesso.jsonlogic.JsonLogic;
import jamsesso.jsonlogic.JsonLogicException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NotExpressionTests {
  @Test
  public void testSingleBoolean() throws JsonLogicException {
    assertEquals(true, JsonLogic.apply("{\"!\": false}", null));
  }

  @Test
  public void testSingleNumber() throws JsonLogicException {
    assertEquals(true, JsonLogic.apply("{\"!\": 0}", null));
  }

  @Test
  public void testSingleString() throws JsonLogicException {
    assertEquals(true, JsonLogic.apply("{\"!\": \"\"}", null));
  }

  @Test
  public void testSingleArray() throws JsonLogicException {
    assertEquals(true, JsonLogic.apply("{\"!\": []}", null));
  }

  @Test
  public void testDoubleBoolean() throws JsonLogicException {
    assertEquals(false, JsonLogic.apply("{\"!!\": false}", null));
  }

  @Test
  public void testDoubleNumber() throws JsonLogicException {
    assertEquals(false, JsonLogic.apply("{\"!!\": 0}", null));
  }

  @Test
  public void testDoubleString() throws JsonLogicException {
    assertEquals(false, JsonLogic.apply("{\"!!\": \"\"}", null));
  }

  @Test
  public void testDoubleArray() throws JsonLogicException {
    assertEquals(false, JsonLogic.apply("{\"!!\": [[]]}", null));
  }
}
