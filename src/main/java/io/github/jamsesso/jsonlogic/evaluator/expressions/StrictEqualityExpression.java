package io.github.jamsesso.jsonlogic.evaluator.expressions;

import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;

import java.math.BigDecimal;
import java.util.List;

public class StrictEqualityExpression implements PreEvaluatedArgumentsExpression {
  public static final StrictEqualityExpression INSTANCE = new StrictEqualityExpression();

  private StrictEqualityExpression() {
    // Only one instance can be constructed. Use StrictEqualityExpression.INSTANCE
  }

  @Override
  public String key() {
    return "===";
  }

  @Override
  public Object evaluate(List arguments, Object data) throws JsonLogicEvaluationException {
    if (arguments.size() != 2) {
      throw new JsonLogicEvaluationException("equality expressions expect exactly 2 arguments");
    }

    Object left = arguments.get(0);
    Object right = arguments.get(1);

    if (left instanceof BigDecimal && right instanceof BigDecimal) {
      return ((BigDecimal) left).compareTo((BigDecimal) right) == 0;
    }

    if (left == right) {
      return true;
    }

    return left != null && left.equals(right);
  }
}
