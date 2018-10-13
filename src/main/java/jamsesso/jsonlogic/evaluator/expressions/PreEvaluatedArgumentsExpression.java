package jamsesso.jsonlogic.evaluator.expressions;

import jamsesso.jsonlogic.ast.JsonLogicArray;
import jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import jamsesso.jsonlogic.evaluator.JsonLogicExpression;
import jamsesso.jsonlogic.utils.ArrayLike;

import java.util.List;

public interface PreEvaluatedArgumentsExpression extends JsonLogicExpression {
  Object evaluate(List arguments, Object data) throws JsonLogicEvaluationException;

  @Override
  default Object evaluate(JsonLogicEvaluator evaluator, JsonLogicArray arguments, Object data)
    throws JsonLogicEvaluationException {
    List<Object> values = evaluator.evaluate(arguments, data);

    if (values.size() == 1 && ArrayLike.isEligible(values.get(0))) {
      values = new ArrayLike(values.get(0));
    }

    return evaluate(values, data);
  }
}
