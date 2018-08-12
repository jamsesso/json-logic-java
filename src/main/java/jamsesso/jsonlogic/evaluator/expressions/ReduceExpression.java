package jamsesso.jsonlogic.evaluator.expressions;

import jamsesso.jsonlogic.JsonLogic;
import jamsesso.jsonlogic.ast.JsonLogicArray;
import jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import jamsesso.jsonlogic.evaluator.JsonLogicExpression;
import jamsesso.jsonlogic.utils.IndexedStructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
  public Object evaluate(JsonLogicEvaluator evaluator, JsonLogicArray arguments, Object data)
    throws JsonLogicEvaluationException {
    if (arguments.size() != 3) {
      throw new JsonLogicEvaluationException("reduce expects exactly 3 arguments");
    }

    Object maybeArray = evaluator.evaluate(arguments.get(0), data);

    if (!IndexedStructure.isEligible(maybeArray)) {
      throw new JsonLogicEvaluationException("first argument to reduce must be a valid array");
    }

    IndexedStructure list = new IndexedStructure(maybeArray);
    Map<String, Object> context = new HashMap<>();

    // Configure the default context.
    context.put("accumulator", evaluator.evaluate(arguments.get(2), data));

    for (Object item : list) {
      context.put("current", item);
      context.put("accumulator", evaluator.evaluate(arguments.get(1), context));
    }

    return context.get("accumulator");
  }
}
