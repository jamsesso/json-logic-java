package jamsesso.jsonlogic.evaluator.expressions;

import jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import jamsesso.jsonlogic.utils.ArrayLike;

import java.util.List;

public class InExpression implements PreEvaluatedArgumentsExpression {
  public static final InExpression INSTANCE = new InExpression();

  private InExpression() {
    // Use INSTANCE instead.
  }

  @Override
  public String key() {
    return "in";
  }

  @Override
  public Object evaluate(List arguments, Object data) throws JsonLogicEvaluationException {
    if (arguments.size() < 2) {
      throw new JsonLogicEvaluationException("in expects exactly 2 arguments");
    }

    // Handle string in (substring)
    if (arguments.get(1) instanceof String) {
      return ((String) arguments.get(1)).contains(arguments.get(0).toString());
    }

    if (ArrayLike.isEligible(arguments.get(1))) {
      return new ArrayLike(arguments.get(1)).contains(arguments.get(0));
    }

    throw new JsonLogicEvaluationException("in expects the second argument to be either a string or array");
  }
}
