package io.github.jamsesso.jsonlogic.evaluator.expressions;

import io.github.jamsesso.jsonlogic.utils.ArrayLike;
import io.github.jamsesso.jsonlogic.JsonLogic;
import io.github.jamsesso.jsonlogic.ast.JsonLogicArray;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicExpression;

import java.util.ArrayList;
import java.util.List;

public class FilterExpression implements JsonLogicExpression {
  public static final FilterExpression INSTANCE = new FilterExpression();

  private FilterExpression() {
    // Use INSTANCE instead.
  }

  @Override
  public String key() {
    return "filter";
  }

  @Override
  public Object evaluate(JsonLogicEvaluator evaluator, JsonLogicArray arguments, Object data, String jsonPath)
    throws JsonLogicEvaluationException {
    if (arguments.size() != 2) {
      throw new JsonLogicEvaluationException("filter expects exactly 2 arguments", jsonPath);
    }

    Object maybeArray = evaluator.evaluate(arguments.get(0), data, jsonPath + "[0]");

    if (!ArrayLike.isEligible(maybeArray)) {
      throw new JsonLogicEvaluationException("first argument to filter must be a valid array", jsonPath + "[0]");
    }

    List<Object> result = new ArrayList<>();

    for (Object item : new ArrayLike(maybeArray)) {
      if(JsonLogic.truthy(evaluator.evaluate(arguments.get(1), item, jsonPath + "[1]"))) {
        result.add(item);
      }
    }

    return result;
  }
}
