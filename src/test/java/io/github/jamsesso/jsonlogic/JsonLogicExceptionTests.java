package io.github.jamsesso.jsonlogic;

import org.junit.Test;
import org.junit.Assert;

import static org.junit.Assert.*;

public class JsonLogicExceptionTests {

  @Test
  public void testExceptionWithoutJsonPath() {
    JsonLogicException exception = new JsonLogicException("Test message");
    assertEquals("Test message", exception.getMessage());
    assertEquals("", exception.getJsonPath());
  }

  @Test
  public void testExceptionWithJsonPath() {
    JsonLogicException exception = new JsonLogicException("Test message", "$.rules[0]");
    assertEquals("Test message", exception.getMessage());
    assertEquals("$.rules[0]", exception.getJsonPath());
  }

  @Test
  public void testExceptionWithCauseAndJsonPath() {
    Throwable cause = new IllegalArgumentException("Cause message");
    JsonLogicException exception = new JsonLogicException(cause, "$.data.field");
    assertEquals("java.lang.IllegalArgumentException: Cause message", exception.getMessage());
    assertEquals("$.data.field", exception.getJsonPath());
    assertSame(cause, exception.getCause());
  }

  @Test
  public void testExceptionWithMessageCauseAndJsonPath() {
    Throwable cause = new IllegalArgumentException("Cause message");
    JsonLogicException exception = new JsonLogicException("Custom message", cause, "$.config");
    assertEquals("Custom message", exception.getMessage());
    assertEquals("$.config", exception.getJsonPath());
    assertSame(cause, exception.getCause());
  }

  @Test
  public void testPrependPartialJsonPath() {
    JsonLogicException exception = new JsonLogicException("Test message");
    exception.prependPartialJsonPath(".field1");
    assertEquals(".field1", exception.getJsonPath());
  }

  @Test
  public void testMultiplePrependsJsonPath() {
    JsonLogicException exception = new JsonLogicException("Test message");
    exception.prependPartialJsonPath(".field2");
    exception.prependPartialJsonPath("[1]");
    exception.prependPartialJsonPath("$.array");
    assertEquals("$.array[1].field2", exception.getJsonPath());
  }

  @Test
  public void testPrependPartialJsonPathWithNullValue() {
    JsonLogicException exception = new JsonLogicException("Test message", "$.original");
    exception.prependPartialJsonPath(null);
    // Path should remain unchanged when prepending null
    assertEquals("$.original", exception.getJsonPath());
  }

  @Test
  public void testExceptionWithNullJsonPath() {
    JsonLogicException exception = new JsonLogicException("Test message", (String) null);
    assertEquals("Test message", exception.getMessage());
    assertEquals("", exception.getJsonPath());
  }

  @Test
  public void testComplexJsonPathConstruction() {
    JsonLogicException exception = new JsonLogicException("Validation error");
    exception.prependPartialJsonPath(".condition");
    exception.prependPartialJsonPath("[2]");
    exception.prependPartialJsonPath(".rules");
    exception.prependPartialJsonPath("$");
    assertEquals("$.rules[2].condition", exception.getJsonPath());
  }

  @Test
  public void testExceptionWithCauseOnly() {
    Throwable cause = new RuntimeException("Root cause");
    JsonLogicException exception = new JsonLogicException(cause);
    assertEquals("java.lang.RuntimeException: Root cause", exception.getMessage());
    assertEquals("", exception.getJsonPath());
    assertSame(cause, exception.getCause());
  }

  @Test
  public void testJsonPathPreservesInsertionOrder() {
    JsonLogicException exception = new JsonLogicException("Test error", "end");
    exception.prependPartialJsonPath("middle");
    exception.prependPartialJsonPath("start");
    assertEquals("startmiddleend", exception.getJsonPath());
  }

  @Test
  public void testEmptyJsonPathInitially() {
    JsonLogicException exception = new JsonLogicException("Test message");
    assertNotNull("JsonPath should not be null", exception.getJsonPath());
    assertEquals("JsonPath should be empty initially", "", exception.getJsonPath());
  }
}

