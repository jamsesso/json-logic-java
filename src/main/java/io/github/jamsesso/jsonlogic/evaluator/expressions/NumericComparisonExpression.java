package io.github.jamsesso.jsonlogic.evaluator.expressions;

import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.utils.BigDecimalOperations;

import java.math.BigDecimal;
import java.util.List;

public class NumericComparisonExpression implements PreEvaluatedArgumentsExpression {
  public static final NumericComparisonExpression GT = new NumericComparisonExpression(">");
  public static final NumericComparisonExpression GTE = new NumericComparisonExpression(">=");
  public static final NumericComparisonExpression LT = new NumericComparisonExpression("<");
  public static final NumericComparisonExpression LTE = new NumericComparisonExpression("<=");

  private final String key;

  private NumericComparisonExpression(String key) {
    this.key = key;
  }

  @Override
  public String key() {
    return key;
  }

  @Override
  public Object evaluate(List arguments, Object data) throws JsonLogicEvaluationException {
    // Convert the arguments to doubles
    int n = Math.min(arguments.size(), 3);

    if (n < 2) {
      throw new JsonLogicEvaluationException("'" + key + "' requires at least 2 arguments");
    }

    BigDecimal[] values = new BigDecimal[n];

    for (int i = 0; i < n; i++) {
      Object value = arguments.get(i);

      if (value instanceof String) {
        try {
          values[i] = new BigDecimal((String) value);
        }
        catch (NumberFormatException e) {
          return false;
        }
      }
      else if (!(value instanceof Number)) {
        return false;
      }
      else {
        values[i] = BigDecimalOperations.fromNumber((Number) value);
      }
    }

    // Handle between comparisons (supported for < and <=)
    if (arguments.size() == 3) {
      switch (key) {
        case "<":
          return values[0].compareTo(values[1]) < 0 && values[1].compareTo(values[2]) < 0;
        case "<=":
          return values[0].compareTo(values[1]) <= 0 && values[1].compareTo(values[2]) <= 0;
        default:
          throw new JsonLogicEvaluationException("'" + key + "' does not support between comparisons");
      }
    }

    // Handle regular comparisons
    switch (key) {
      case "<":
        return values[0].compareTo(values[1]) < 0;
      case "<=":
        return values[0].compareTo(values[1]) <= 0;
      case ">":
        return values[0].compareTo(values[1]) > 0;
      case ">=":
        return values[0].compareTo(values[1]) >= 0;
      default:
        throw new JsonLogicEvaluationException("'" + key + "' is not a comparison expression");
    }
  }
}
