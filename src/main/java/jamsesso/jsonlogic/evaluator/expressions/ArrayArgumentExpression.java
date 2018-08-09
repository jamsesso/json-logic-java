package jamsesso.jsonlogic.evaluator.expressions;

import jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;

import java.util.List;

public interface ArrayArgumentExpression extends PreEvaluatedArgumentsExpression {
  Object evaluate(List arguments, Object data) throws JsonLogicEvaluationException;

  @Override
  default Object evaluate(Object argument, Object data) throws JsonLogicEvaluationException {
    if (!(argument instanceof List)) {
      throw new JsonLogicEvaluationException("'" + key() + "' requires an array as an argument");
    }

    List arguments = (List) argument;
    return evaluate(arguments, data);
  }
}
