package jamsesso.jsonlogic.evaluator.expressions;

import jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;

import java.util.List;
import java.util.function.BiFunction;

public class MathExpression implements PreEvaluatedArgumentsExpression {
  public static final MathExpression ADD = new MathExpression("+", (a, b) -> a + b);
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
  public Object evaluate(Object argument, Object data) throws JsonLogicEvaluationException {
    if (key.equals("+") && argument instanceof String) {
      try {
        return Double.parseDouble((String) argument);
      }
      catch (NumberFormatException e) {
        throw new JsonLogicEvaluationException(e);
      }
    }

    if (key.equals("-") && argument instanceof Number) {
      return -1 * ((Number) argument).doubleValue();
    }

    if (!(argument instanceof List)) {
      throw new JsonLogicEvaluationException("'" + key() + "' requires an array as an argument");
    }

    List arguments = (List) argument;

    if (arguments.isEmpty()) {
      return null;
    }

    // Collect all of the arguments
    double[] values = new double[arguments.size()];

    for (int i = 0; i < arguments.size(); i++) {
      Object value = arguments.get(i);

      if (!(value instanceof Number)) {
        return null;
      }

      values[i] = ((Number) value).doubleValue();
    }

    // Reduce the values into a single result
    double accumulator = values[0];

    for (int i = 1; i < values.length && (i < maxArguments || maxArguments == 0); i++) {
      accumulator = reducer.apply(accumulator, values[i]);
    }

    return accumulator;
  }
}
