package jamsesso.jsonlogic.evaluator;

import jamsesso.jsonlogic.ast.JsonLogicNode;

public interface JsonLogicExpression {
  String key();

  Object evaluate(JsonLogicEvaluator evaluator, JsonLogicNode argument, Object data)
    throws JsonLogicEvaluationException;
}
