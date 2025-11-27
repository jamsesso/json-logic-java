package io.github.jamsesso.jsonlogic.evaluator.expressions;

import io.github.jamsesso.jsonlogic.ast.JsonLogicArray;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicExpression;

public abstract class JsonPathHandlerJsonLogicExpression implements JsonLogicExpression {
  @Override
  public Object evaluate(JsonLogicEvaluator evaluator, JsonLogicArray arguments, Object data, String jsonPath) throws JsonLogicEvaluationException {
    try {
      return evaluate(evaluator, arguments, data);
    } catch (JsonLogicEvaluationException e) {
      e.prependPartialJsonPath(jsonPath);
      throw e;
    }
  }

  public abstract Object evaluate(JsonLogicEvaluator evaluator, JsonLogicArray arguments, Object data) throws JsonLogicEvaluationException;
}
