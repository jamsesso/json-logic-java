package io.github.jamsesso.jsonlogic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NotExpressionTests {
  private static final JsonLogic jsonLogic = new JsonLogic();

  @Test
  public void testSingleBoolean() throws JsonLogicException {
    assertEquals(true, jsonLogic.apply("{\"!\": false}", null));
  }

  @Test
  public void testSingleNumber() throws JsonLogicException {
    assertEquals(true, jsonLogic.apply("{\"!\": 0}", null));
  }

  @Test
  public void testSingleString() throws JsonLogicException {
    assertEquals(true, jsonLogic.apply("{\"!\": \"\"}", null));
  }

  @Test
  public void testSingleArray() throws JsonLogicException {
    assertEquals(true, jsonLogic.apply("{\"!\": []}", null));
  }

  @Test
  public void testDoubleBoolean() throws JsonLogicException {
    assertEquals(false, jsonLogic.apply("{\"!!\": false}", null));
  }

  @Test
  public void testDoubleNumber() throws JsonLogicException {
    assertEquals(false, jsonLogic.apply("{\"!!\": 0}", null));
  }

  @Test
  public void testDoubleString() throws JsonLogicException {
    assertEquals(false, jsonLogic.apply("{\"!!\": \"\"}", null));
  }

  @Test
  public void testDoubleArray() throws JsonLogicException {
    assertEquals(false, jsonLogic.apply("{\"!!\": [[]]}", null));
  }
}
