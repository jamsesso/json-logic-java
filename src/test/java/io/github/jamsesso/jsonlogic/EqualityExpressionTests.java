package io.github.jamsesso.jsonlogic;

import org.junit.Test;

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
  public void equalityOfDates() throws JsonLogicException {
    assertEquals(
            true,
            jsonLogic.apply(
                    "{\"==\": [{\"date\": [\"2024-05-28T00:00:00Z\"]}, {\"date\": [\"2024-05-28T00:00:00Z\"]}]}",
                    null
            )
    );
  }

  @Test
  public void equalityOfTwoDifferentValidDates() throws JsonLogicException {
    assertEquals(
            false,
            jsonLogic.apply(
                    "{\"==\": [{\"date\": [\"2024-06-28T00:00:00Z\"]}, {\"date\": [\"2024-05-28T00:00:00Z\"]}]}",
                    null
            )
    );
  }

  @Test
  public void equalityOfDateAndDateString() throws JsonLogicException {
    assertEquals(
            true,
            jsonLogic.apply(
                    "{\"==\": [{\"date\": [\"2024-05-28T00:00:00Z\"]}, \"2024-05-28T00:00:00Z\"]}",
                    null
            )
    );
  }

  @Test
  public void equalityOfDateStringAndDate() throws JsonLogicException {
    assertEquals(
            true,
            jsonLogic.apply(
                    "{\"==\": [\"2024-05-28T00:00:00Z\", {\"date\": [\"2024-05-28T00:00:00Z\"]}]}",
                    null
            )
    );
  }

  @Test
  public void equalityOfInvalidDate() throws JsonLogicException {
    assertEquals(
            false,
            jsonLogic.apply("{\"==\": [\"2024-05-28\", {\"date\": [\"2024-05-28T00:00:00Z\"]}]}", null)
    );
  }
}
