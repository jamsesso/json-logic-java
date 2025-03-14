package io.github.jamsesso.jsonlogic.evaluator.expressions;

import io.github.jamsesso.jsonlogic.utils.ArrayLike;
import io.github.jamsesso.jsonlogic.ast.JsonLogicArray;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicExpression;

import java.util.HashMap;
import java.util.Map;

public class ReduceExpression implements JsonLogicExpression {
  public static final ReduceExpression INSTANCE = new ReduceExpression();

  private ReduceExpression() {
    // Use INSTANCE instead.
  }

  @Override
  public String key() {
    return "reduce";
  }

  @Override
  public Object evaluate(JsonLogicEvaluator evaluator, JsonLogicArray arguments, Object data, String jsonPath)
    throws JsonLogicEvaluationException {
    if (arguments.size() != 3) {
      throw new JsonLogicEvaluationException("reduce expects exactly 3 arguments", jsonPath);
    }

    Object maybeArray = evaluator.evaluate(arguments.get(0), data, jsonPath + "[0]");
    Object accumulator = evaluator.evaluate(arguments.get(2), data, jsonPath + "[2]");

    if (!ArrayLike.isEligible(maybeArray)) {
      return accumulator;
    }

    Map<String, Object> context = new HashMap<>();
    context.put("accumulator", accumulator);

    for (Object item : new ArrayLike(maybeArray)) {
      context.put("current", item);
      context.put("accumulator", evaluator.evaluate(arguments.get(1), context, jsonPath + "[1]"));
    }

    return context.get("accumulator");
  }
}
