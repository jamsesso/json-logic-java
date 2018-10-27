package io.github.jamsesso.jsonlogic.evaluator.expressions;

import io.github.jamsesso.jsonlogic.ast.JsonLogicArray;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicExpression;

public class StrictInequalityExpression implements JsonLogicExpression {
  public static final StrictInequalityExpression INSTANCE =
    new StrictInequalityExpression(StrictEqualityExpression.INSTANCE);

  private final StrictEqualityExpression delegate;

  private StrictInequalityExpression(StrictEqualityExpression delegate) {
    this.delegate = delegate;
  }

  @Override
  public String key() {
    return "!==";
  }

  @Override
  public Object evaluate(JsonLogicEvaluator evaluator, JsonLogicArray arguments, Object data)
    throws JsonLogicEvaluationException {
    boolean result = (boolean) delegate.evaluate(evaluator, arguments, data);

    return !result;
  }
}
