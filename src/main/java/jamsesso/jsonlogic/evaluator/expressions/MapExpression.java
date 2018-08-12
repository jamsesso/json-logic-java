package jamsesso.jsonlogic.evaluator.expressions;

import jamsesso.jsonlogic.ast.JsonLogicArray;
import jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import jamsesso.jsonlogic.evaluator.JsonLogicExpression;
import jamsesso.jsonlogic.utils.IndexedStructure;

import java.util.ArrayList;
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
  public Object evaluate(JsonLogicEvaluator evaluator, JsonLogicArray arguments, Object data)
    throws JsonLogicEvaluationException {
    if (arguments.size() != 2) {
      throw new JsonLogicEvaluationException("map expects exactly 2 arguments");
    }

    Object maybeArray = evaluator.evaluate(arguments.get(0), data);

    if (!IndexedStructure.isEligible(maybeArray)) {
      throw new JsonLogicEvaluationException("first argument to map must be a valid array");
    }

    IndexedStructure list = new IndexedStructure(maybeArray);
    List<Object> result = new ArrayList<>();

    for (Object item : list) {
      result.add(evaluator.evaluate(arguments.get(1), item));
    }

    return result;
  }
}
