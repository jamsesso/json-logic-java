package jamsesso.jsonlogic.evaluator;

import jamsesso.jsonlogic.ast.JsonLogicArray;

public interface JsonLogicExpression {
  String key();

  Object evaluate(JsonLogicEvaluator evaluator, JsonLogicArray argument, Object data)
    throws JsonLogicEvaluationException;
}
