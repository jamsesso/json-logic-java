package io.github.jamsesso.jsonlogic.evaluator.expressions;

import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.utils.ArrayLike;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.BiFunction;

public class MathExpression implements PreEvaluatedArgumentsExpression {
  public static final MathExpression ADD = new MathExpression("+", BigDecimal::add);
  public static final MathExpression SUBTRACT = new MathExpression("-", BigDecimal::subtract, 2);
  public static final MathExpression MULTIPLY = new MathExpression("*", BigDecimal::multiply);
  public static final MathExpression DIVIDE = new MathExpression("/",
          (a, b) -> a.divide(b, 10, RoundingMode.HALF_UP), 2);
  public static final MathExpression MODULO = new MathExpression("%", BigDecimal::remainder, 2);
  public static final MathExpression MIN = new MathExpression("min", BigDecimal::min);
  public static final MathExpression MAX = new MathExpression("max", BigDecimal::max);

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
  public Object evaluate(List arguments, Object data, String jsonPath) throws JsonLogicEvaluationException {
    if (arguments.isEmpty()) {
      return null;
    }

    // Collect all of the arguments
    BigDecimal[] values = new BigDecimal[arguments.size()];

    for (int i = 0; i < arguments.size(); i++) {
      Object value = arguments.get(i);

      // 处理 null 和空字符串，默认为 0
      if (value == null || "".equals(value)) {
        values[i] = BigDecimal.ZERO;
        continue;
      }

      if (key.equals("*") || key.equals("+")) {
        while (ArrayLike.isEligible(value)) {
          value = new ArrayLike(value).get(0);
        }
      }

      if (value instanceof String) {
        try {
          values[i] = new BigDecimal((String) value);
        } catch (NumberFormatException e) {
          return null;
        }
      } else if (value instanceof BigDecimal) {
        values[i] = (BigDecimal) value;
      } else if (value instanceof Number) {
        values[i] = new BigDecimal(value.toString());
      } else {
        return null;
      }
    }

    if (values.length == 1) {
      if (key.equals("-")) {
        return values[0].negate();
      }
      if (key.equals("/")) {
        return null;
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