package io.github.jamsesso.jsonlogic.evaluator.expressions;

import io.github.jamsesso.jsonlogic.JsonLogic;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;

public class EqualityExpression implements PreEvaluatedArgumentsExpression {
  public static final EqualityExpression INSTANCE = new EqualityExpression();

  private EqualityExpression() {
    // Only one instance can be constructed. Use EqualityExpression.INSTANCE
  }

  @Override
  public String key() {
    return "==";
  }

  @Override
  public Object evaluate(List arguments, Object data) throws JsonLogicEvaluationException {
    if (arguments.size() != 2) {
      throw new JsonLogicEvaluationException("equality expressions expect exactly 2 arguments");
    }

    Object left = arguments.get(0);
    Object right = arguments.get(1);

    // Use the loose equality matrix
    // https://developer.mozilla.org/en-US/docs/Web/JavaScript/Equality_comparisons_and_sameness#Loose_equality_using
    if (left == null && right == null) {
      return true;
    }

    if (left == null || right == null) {
      return false;
    }

    // Check numeric loose equality
    if (left instanceof Number && right instanceof Number) {
      return Double.valueOf(((Number) left).doubleValue()).equals(((Number) right).doubleValue());
    }

    if (left instanceof Number && right instanceof String) {
      return compareNumberToString((Number) left, (String) right);
    }

    if (left instanceof Number && right instanceof Boolean) {
      return compareNumberToBoolean((Number) left, (Boolean) right);
    }


    if (left instanceof String && right instanceof String) {
      try
      {
        Double leftDouble = (double) Instant.parse((String) left).getEpochSecond();
        Double rightDouble = (double) Instant.parse((String) right).getEpochSecond();
        return leftDouble.equals(rightDouble);
      }
      catch (DateTimeParseException e)
      {
        // Check string loose equality
        return left.equals(right);
      }
    }

    if (left instanceof String && right instanceof Number) {
      return compareNumberToString((Number) right, (String) left);
    }

    if (left instanceof String && right instanceof Boolean) {
      return compareStringToBoolean((String) left, (Boolean) right);
    }

    // Check boolean loose equality
    if (left instanceof Boolean && right instanceof Boolean) {
      return ((Boolean) left).booleanValue() == ((Boolean) right).booleanValue();
    }

    if (left instanceof Boolean && right instanceof Number) {
      return compareNumberToBoolean((Number) right, (Boolean) left);
    }

    if (left instanceof Boolean && right instanceof String) {
      return compareStringToBoolean((String) right, (Boolean) left);
    }

    // Check non-truthy values
    return !JsonLogic.truthy(left) && !JsonLogic.truthy(right);

  }

  private boolean compareNumberToString(Number left, String right) {
    try {
      if (right.trim().isEmpty()) {
        right = "0";
      }

      return Double.parseDouble(right) == left.doubleValue();
    }
    catch (NumberFormatException e) {
      return false;
    }
  }

  private boolean compareNumberToBoolean(Number left, Boolean right) {
    if (right) {
      return left.doubleValue() == 1.0;
    }

    return left.doubleValue() == 0.0;
  }

  private boolean compareStringToBoolean(String left, Boolean right) {
    return JsonLogic.truthy(left) == right;
  }
}
