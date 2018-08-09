package jamsesso.jsonlogic.evaluator.expressions;

import jamsesso.jsonlogic.ast.JsonLogicNode;
import jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import jamsesso.jsonlogic.evaluator.JsonLogicExpression;

public interface PreEvaluatedArgumentsExpression extends JsonLogicExpression {
  Object evaluate(Object argument, Object data) throws JsonLogicEvaluationException;

  @Override
  default Object evaluate(JsonLogicEvaluator evaluator, JsonLogicNode argument, Object data)
    throws JsonLogicEvaluationException {
    return evaluate(evaluator.evaluate(argument, data), data);
  }
}
