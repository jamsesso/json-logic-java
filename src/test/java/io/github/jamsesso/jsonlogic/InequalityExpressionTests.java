package io.github.jamsesso.jsonlogic;

import org.junit.Test;

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
  public void inequalityOfDates() throws JsonLogicException {
    assertEquals(
            true,
            jsonLogic.apply(
                    "{\"!=\": [{\"date\": [\"2024-06-28T00:00:00Z\"]}, {\"date\": [\"2024-05-28T00:00:00Z\"]}]}",
                    null
            )
    );
  }

  @Test
  public void inequalityOfTwoDifferentValidDates() throws JsonLogicException {
    assertEquals(
            true,
            jsonLogic.apply(
                    "{\"!=\": [{\"date\": [\"2024-06-28T00:00:00Z\"]}, {\"date\": [\"2024-05-28T00:00:00Z\"]}]}",
                    null
            )
    );
  }

  @Test
  public void inequalityOfDateAndDateString() throws JsonLogicException {
    assertEquals(
            true,
            jsonLogic.apply(
                    "{\"!=\": [{\"date\": [\"2024-06-28T00:00:00Z\"]}, \"2024-05-28T00:00:00Z\"]}",
                    null
            )
    );
  }

  @Test
  public void inequalityOfDateStringAndDate() throws JsonLogicException {
    assertEquals(
            true,
            jsonLogic.apply(
                    "{\"!=\": [\"2024-06-28T00:00:00Z\", {\"date\": [\"2024-05-28T00:00:00Z\"]}]}",
                    null
            )
    );
  }

  @Test
  public void inequalityOfInvalidDate() throws JsonLogicException {
    assertEquals(
            true,
            jsonLogic.apply("{\"!=\": [\"2024-05-28\", {\"date\": [\"2024-05-28T00:00:00Z\"]}]}", null)
    );
  }
}
