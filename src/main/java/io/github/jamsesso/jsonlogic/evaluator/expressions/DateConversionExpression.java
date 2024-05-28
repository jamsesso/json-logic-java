package io.github.jamsesso.jsonlogic.evaluator.expressions;

import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.utils.DateOperations;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class DateConversionExpression implements PreEvaluatedArgumentsExpression{
    public static final DateConversionExpression INSTANCE = new DateConversionExpression();

    @Override
    public String key() {
        return "date";
    }

    @Override
    public Object evaluate(List arguments, Object data) throws JsonLogicEvaluationException {
        if (arguments.isEmpty()) {
            return null;
        }
        if (arguments.size() != 1) {
            throw new JsonLogicEvaluationException("'date' requires only 1 argument");
        }
        // Convert the argument to OffsetDateTime
        Object value = arguments.get(0);
        if (value instanceof String) {
            try {
                return DateOperations.fromString((String) value);
            }
            catch (DateTimeParseException e) {
                throw new JsonLogicEvaluationException(e);
            }
        }
        return false;
    }
}
