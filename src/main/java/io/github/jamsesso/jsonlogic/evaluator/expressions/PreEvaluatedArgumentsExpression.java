package io.github.jamsesso.jsonlogic.evaluator.expressions;

import io.github.jamsesso.jsonlogic.utils.ArrayLike;
import io.github.jamsesso.jsonlogic.ast.JsonLogicArray;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicExpression;

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
