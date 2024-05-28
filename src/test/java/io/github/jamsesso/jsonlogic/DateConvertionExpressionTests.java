package io.github.jamsesso.jsonlogic;

import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import org.junit.Test;

import java.time.OffsetDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DateConvertionExpressionTests {
    private static final JsonLogic jsonLogic = new JsonLogic();

    @Test
    public void convertValidDateTimeString() throws JsonLogicException {
        assertEquals(
                OffsetDateTime.parse("2024-05-28T00:00:00Z"),
                jsonLogic.apply("{\"date\": [\"2024-05-28T00:00:00Z\"]}", null)
        );
    }

    @Test
    public void convertInvalidDateTimeString() {
        try {
            jsonLogic.apply("{\"date\": [\"2024-05-28\"]}", null);
        } catch (JsonLogicException e) {
            assertTrue(e instanceof JsonLogicEvaluationException);
            assertEquals(
                    e.getMessage(),
                    "java.time.format.DateTimeParseException: Text '2024-05-28' could not be parsed at index 10"
            );
        }
    }

    @Test
    public void invalidNumberOfArguments() {
        try {
            jsonLogic.apply("{\"date\": [\"2024-05-28T00:00:00Z\", 1]}", null);
        } catch (JsonLogicException e) {
            assertTrue(e instanceof JsonLogicEvaluationException);
            assertEquals(e.getMessage(), "'date' requires only 1 argument");
        }
    }
}
