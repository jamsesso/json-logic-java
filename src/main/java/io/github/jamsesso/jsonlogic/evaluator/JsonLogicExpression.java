package io.github.jamsesso.jsonlogic.evaluator;

import io.github.jamsesso.jsonlogic.ast.JsonLogicArray;

public interface JsonLogicExpression {
  String key();

  Object evaluate(JsonLogicEvaluator evaluator, JsonLogicArray arguments, Object data, String jsonPath)
    throws JsonLogicEvaluationException;
}
