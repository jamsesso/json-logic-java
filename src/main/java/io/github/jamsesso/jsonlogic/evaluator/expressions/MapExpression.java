package io.github.jamsesso.jsonlogic.evaluator.expressions;

import io.github.jamsesso.jsonlogic.ast.JsonLogicArray;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicExpression;
import io.github.jamsesso.jsonlogic.utils.ArrayLike;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapExpression implements JsonLogicExpression {
  public static final MapExpression INSTANCE = new MapExpression();

  private MapExpression() {
    // Use INSTANCE instead.
  }

  @Override
  public String key() {
    return "map";
  }

  @Override
  public Object evaluate(JsonLogicEvaluator evaluator, JsonLogicArray arguments, Object data, String jsonPath)
    throws JsonLogicEvaluationException {
    if (arguments.size() != 2) {
      throw new JsonLogicEvaluationException("map expects exactly 2 arguments", jsonPath);
    }

    Object maybeArray = evaluator.evaluate(arguments.get(0), data, jsonPath + "[0]");

    if (!ArrayLike.isEligible(maybeArray)) {
      return Collections.emptyList();
    }

    List<Object> result = new ArrayList<>();

    for (Object item : new ArrayLike(maybeArray)) {
      result.add(evaluator.evaluate(arguments.get(1), item, jsonPath + "[1]"));
    }

    return result;
  }
}
