package io.github.jamsesso.jsonlogic.evaluator.expressions;

import io.github.jamsesso.jsonlogic.ast.JsonLogicArray;
import io.github.jamsesso.jsonlogic.ast.JsonLogicNode;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import io.github.jamsesso.jsonlogic.utils.ArrayLike;

import java.util.ArrayList;
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
  public Object evaluate(JsonLogicEvaluator evaluator, JsonLogicArray arguments, Object data, String jsonPath)
      throws JsonLogicEvaluationException {
    if (evaluator.getClass() != JsonLogicEvaluator.class) {
      return PreEvaluatedArgumentsExpression.super.evaluate(evaluator, arguments, data, jsonPath);
    }
    List<Object> values = new ArrayList<>(arguments.size());
    Boolean indexedMatch = null;
    int index = 0;
    for (JsonLogicNode argument : arguments) {
      String path = String.format("%s[%d]", jsonPath, index++);
      if (index == 2 && argument != null && argument.getClass() == JsonLogicArray.class) {
        indexedMatch = ((JsonLogicArray) argument).indexedStringMatch(values.get(0));
        if (indexedMatch != null) {
          continue;
        }
      }
      values.add(evaluator.evaluate(argument, data, path));
    }
    return indexedMatch != null ? indexedMatch
      : evaluate(PreEvaluatedArgumentsExpression.unpackArguments(values), data, jsonPath);
  }

  @Override
  public Object evaluate(List arguments, Object data, String jsonPath) throws JsonLogicEvaluationException {
    if (arguments.size() < 2) {
      return false;
    }

    // Handle string in (substring)
    if (arguments.get(1) instanceof String) {
      if (arguments.get(0) == null) {
        return false;
      }
      return ((String) arguments.get(1)).contains(arguments.get(0).toString());
    }

    if (ArrayLike.isEligible(arguments.get(1))) {
      return new ArrayLike(arguments.get(1)).contains(arguments.get(0));
    }

    return false;
  }
}
