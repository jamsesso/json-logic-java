package io.github.jamsesso.jsonlogic.evaluator.expressions;

import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.utils.ArrayLike;

import java.util.List;
import java.util.function.BiFunction;

public class MathExpression implements PreEvaluatedArgumentsExpression {
  public static final MathExpression ADD = new MathExpression("+", Double::sum);
  public static final MathExpression SUBTRACT = new MathExpression("-", (a, b) -> a - b, 2);
  public static final MathExpression MULTIPLY = new MathExpression("*", (a, b) -> a * b);
  public static final MathExpression DIVIDE = new MathExpression("/", (a, b) -> a / b, 2);
  public static final MathExpression MODULO = new MathExpression("%", (a, b) -> a % b, 2);
  public static final MathExpression MIN = new MathExpression("min", Math::min);
  public static final MathExpression MAX = new MathExpression("max", Math::max);

  private final String key;
  private final BiFunction<Double, Double, Double> reducer;
  private final int maxArguments;

  public MathExpression(String key, BiFunction<Double, Double, Double> reducer) {
    this(key, reducer, 0);
  }

  public MathExpression(String key, BiFunction<Double, Double, Double> reducer, int maxArguments) {
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
    double[] values = new double[arguments.size()];

    for (int i = 0; i < arguments.size(); i++) {
      Object value = arguments.get(i);

      if (key.equals("*") || key.equals("+")) {
        while (ArrayLike.isEligible(value)) {
          value = new ArrayLike(value).get(0);
        }
      }
      if (value instanceof String) {
        try {
          values[i] = Double.parseDouble((String) value);
        }
        catch (NumberFormatException e) {
          return null;
        }
      }
      else if (!(value instanceof Number)) {
        return null;
      }
      else {
        values[i] = ((Number) value).doubleValue();
      }
    }

    if (values.length == 1) {
      if (key.equals("-")) {
        return -values[0];
      }

      if (key.equals("/")) {
        return null;
      }
    }

    // Reduce the values into a single result
    double accumulator = values[0];

    for (int i = 1; i < values.length && (i < maxArguments || maxArguments == 0); i++) {
      accumulator = reducer.apply(accumulator, values[i]);
    }

    return accumulator;
  }
}
