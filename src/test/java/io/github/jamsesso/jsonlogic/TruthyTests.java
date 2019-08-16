package io.github.jamsesso.jsonlogic;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TruthyTests {
  @Test
  public void testTruthyValues() {
    // Zero
    assertFalse(JsonLogic.truthy(0));

    // Any non-zero number
    assertTrue(JsonLogic.truthy(1.04));
    assertTrue(JsonLogic.truthy(-1));

    // Empty array or collection
    assertFalse(JsonLogic.truthy(Collections.EMPTY_LIST));
    assertFalse(JsonLogic.truthy(new int[0]));

    // Any non-empty array or collection
    assertTrue(JsonLogic.truthy(Collections.singleton(1)));
    assertTrue(JsonLogic.truthy(new boolean[] {false}));

    // Empty string
    assertFalse(JsonLogic.truthy(""));

    // Any non-empty string
    assertTrue(JsonLogic.truthy("hello world"));
    assertTrue(JsonLogic.truthy("0"));

    // Null
    assertFalse(JsonLogic.truthy(null));

    // NaN and Infinity
    assertFalse(JsonLogic.truthy(Double.NaN));
    assertFalse(JsonLogic.truthy(Float.NaN));
    assertTrue(JsonLogic.truthy(Double.POSITIVE_INFINITY));
    assertTrue(JsonLogic.truthy(Double.NEGATIVE_INFINITY));
    assertTrue(JsonLogic.truthy(Float.POSITIVE_INFINITY));
    assertTrue(JsonLogic.truthy(Float.NEGATIVE_INFINITY));
  }
}
