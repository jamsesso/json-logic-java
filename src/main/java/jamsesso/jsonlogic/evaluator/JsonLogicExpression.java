package jamsesso.jsonlogic.evaluator;

import jamsesso.jsonlogic.ast.JsonLogicArray;

public interface JsonLogicExpression {
  String key();

  Object evaluate(JsonLogicEvaluator evaluator, JsonLogicArray arguments, Object data)
    throws JsonLogicEvaluationException;
}
