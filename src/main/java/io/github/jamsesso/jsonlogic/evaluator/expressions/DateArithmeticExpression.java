package io.github.jamsesso.jsonlogic.evaluator.expressions;

import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.utils.BigDecimalOperations;
import io.github.jamsesso.jsonlogic.utils.DateOperations;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public class DateArithmeticExpression implements PreEvaluatedArgumentsExpression {
  public static final DateArithmeticExpression DATE_ADD = new DateArithmeticExpression("+");
  public static final DateArithmeticExpression DATE_SUBTRACT = new DateArithmeticExpression("-");

  private final String key;

  public DateArithmeticExpression(String key) {
    this.key = key;
  }

  @Override
  public String key() {
    return key;
  }

  @Override
  public Object evaluate(List arguments, Object data) throws JsonLogicEvaluationException {
    if (arguments.isEmpty()) {
      return null;
    }

    if (arguments.size() == 3) {
      try {
        OffsetDateTime date = DateOperations.fromString((String) arguments.get(0));
        BigDecimal intervalToAdd = (BigDecimal) arguments.get(1);
        ChronoUnit interval = ChronoUnit.valueOf((String) arguments.get(2));

        return Objects.equals(key, "+")
                ? date.plus(Long.parseLong(intervalToAdd.toString()), interval)
                : date.minus(Long.parseLong(intervalToAdd.toString()), interval);
      } catch (DateTimeParseException | IllegalArgumentException | ClassCastException e) {
        return null;
      }
    } else if (arguments.size() == 2 && Objects.equals(key, "-")) {
      try {
        OffsetDateTime date1 = DateOperations.fromString((String) arguments.get(0));
        OffsetDateTime date2 = DateOperations.fromString((String) arguments.get(1));

        return Duration.between(date1, date2).toDays();
      } catch (DateTimeParseException e) {
        return null;
      }
    } else {
      throw new JsonLogicEvaluationException("date arithmetic requires 2 or 3 arguments");
    }
  }
}
