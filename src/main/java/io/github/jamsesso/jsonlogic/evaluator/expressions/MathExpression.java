package io.github.jamsesso.jsonlogic.evaluator.expressions;

import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.utils.BigDecimalOperations;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;

public class MathExpression implements PreEvaluatedArgumentsExpression {
  public static final MathExpression ADD = new MathExpression("+", BigDecimalOperations::add);
  public static final MathExpression SUBTRACT = new MathExpression("-", BigDecimalOperations::subtract, 2);
  public static final MathExpression MULTIPLY = new MathExpression("*", BigDecimalOperations::multiply);
  public static final MathExpression DIVIDE = new MathExpression("/", BigDecimalOperations::divide, 2);
  public static final MathExpression MODULO = new MathExpression("%", BigDecimalOperations::modulo, 2);
  public static final MathExpression MIN = new MathExpression("min", BigDecimalOperations::min);
  public static final MathExpression MAX = new MathExpression("max", BigDecimalOperations::max);

  private final String key;
  private final BiFunction<BigDecimal, BigDecimal, BigDecimal> reducer;
  private final int maxArguments;

  public MathExpression(String key, BiFunction<BigDecimal, BigDecimal, BigDecimal> reducer) {
    this(key, reducer, 0);
  }

  public MathExpression(String key, BiFunction<BigDecimal, BigDecimal, BigDecimal> reducer, int maxArguments) {
    this.key = key;
    this.reducer = reducer;
    this.maxArguments = maxArguments;
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

    if (arguments.size() == 1) {
      if (key.equals("+") && arguments.get(0) instanceof String) {
        try {
          return new BigDecimal((String) arguments.get(0));
        }
        catch (NumberFormatException e) {
          throw new JsonLogicEvaluationException(e);
        }
      }

      if (key.equals("-") && arguments.get(0) instanceof BigDecimal) {
        return ((BigDecimal) arguments.get(0)).negate();
      }

      if (key.equals("/")) {
        return null;
      }
    }

    // Collect all of the arguments
    BigDecimal[] values = new BigDecimal[arguments.size()];

    for (int i = 0; i < arguments.size(); i++) {
      Object value = arguments.get(i);

      if (value instanceof String) {
        try {
          values[i] = new BigDecimal((String) value);
        }
        catch (NumberFormatException e) {
          return null;
        }
      }
      else if (!(value instanceof Number)) {
        return null;
      }
      else {
        values[i] = BigDecimalOperations.fromNumber((Number) value);
      }
    }

    // Reduce the values into a single result
    BigDecimal accumulator = values[0];

    for (int i = 1; i < values.length && (i < maxArguments || maxArguments == 0); i++) {
      accumulator = reducer.apply(accumulator, values[i]);
    }

    return accumulator;
  }
}
