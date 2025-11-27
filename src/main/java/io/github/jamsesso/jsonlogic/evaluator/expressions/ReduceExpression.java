package io.github.jamsesso.jsonlogic.evaluator.expressions;

import io.github.jamsesso.jsonlogic.ast.JsonLogicArray;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicExpression;
import io.github.jamsesso.jsonlogic.utils.ArrayLike;

import java.util.HashMap;
import java.util.Map;

public class ReduceExpression extends JsonPathHandlerJsonLogicExpression implements JsonLogicExpression {
  public static final ReduceExpression INSTANCE = new ReduceExpression();

  private ReduceExpression() {
    // Use INSTANCE instead.
  }

  @Override
  public String key() {
    return "reduce";
  }

  @Override
  public Object evaluate(JsonLogicEvaluator evaluator, JsonLogicArray arguments, Object data)
      throws JsonLogicEvaluationException {
    if (arguments.size() != 3) {
      throw new JsonLogicEvaluationException("reduce expects exactly 3 arguments");
    }

    Object maybeArray;
    Object accumulator;

    try {
      maybeArray = evaluator.evaluate(arguments.get(0), data);
    } catch (JsonLogicEvaluationException e) {
      e.prependPartialJsonPath("[0]");
      throw e;
    }

    try {
      accumulator = evaluator.evaluate(arguments.get(2), data);
    } catch (JsonLogicEvaluationException e) {
      e.prependPartialJsonPath("[2]");
      throw e;
    }

    if (!ArrayLike.isEligible(maybeArray)) {
      return accumulator;
    }

    Map<String, Object> context = new HashMap<>();
    context.put("accumulator", accumulator);

    for (Object item : new ArrayLike(maybeArray)) {
      context.put("current", item);
      try {
        context.put("accumulator", evaluator.evaluate(arguments.get(1), context));
      } catch (JsonLogicEvaluationException e) {
        e.prependPartialJsonPath("[1]");
        throw e;
      }
    }

    return context.get("accumulator");
  }
}
