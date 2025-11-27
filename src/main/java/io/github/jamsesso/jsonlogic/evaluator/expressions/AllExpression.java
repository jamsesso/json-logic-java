package io.github.jamsesso.jsonlogic.evaluator.expressions;

import io.github.jamsesso.jsonlogic.JsonLogic;
import io.github.jamsesso.jsonlogic.ast.JsonLogicArray;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicExpression;
import io.github.jamsesso.jsonlogic.utils.ArrayLike;

public class AllExpression extends JsonPathHandlerJsonLogicExpression implements JsonLogicExpression {
  public static final AllExpression INSTANCE = new AllExpression();

  private AllExpression() {
    // Use INSTANCE instead.
  }

  @Override
  public String key() {
    return "all";
  }

  @Override
  public Object evaluate(JsonLogicEvaluator evaluator, JsonLogicArray arguments, Object data)
      throws JsonLogicEvaluationException {
    if (arguments.size() != 2) {
      throw new JsonLogicEvaluationException("all expects exactly 2 arguments");
    }

    Object maybeArray;
    try {
      maybeArray = evaluator.evaluate(arguments.get(0), data);
    } catch (JsonLogicEvaluationException e) {
      e.prependPartialJsonPath("[0]");
      throw e;
    }

    if (maybeArray == null) {
      return false;
    }

    if (!ArrayLike.isEligible(maybeArray)) {
      throw new JsonLogicEvaluationException("first argument to all must be a valid array");
    }

    ArrayLike array = new ArrayLike(maybeArray);

    if (array.size() < 1) {
      return false;
    }

    int index = 1;
    for (Object item : array) {
      try {
        if (!JsonLogic.truthy(evaluator.evaluate(arguments.get(1), item))) {
          return false;
        }
      } catch (JsonLogicEvaluationException e) {
        e.prependPartialJsonPath("[" + index + "]");
        throw e;
      }
    }

    return true;
  }
}
