package jamsesso.jsonlogic.evaluator.expressions;

import jamsesso.jsonlogic.ast.JsonLogicArray;
import jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import jamsesso.jsonlogic.evaluator.JsonLogicExpression;

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
