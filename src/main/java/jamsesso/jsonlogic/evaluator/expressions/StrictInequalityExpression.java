package jamsesso.jsonlogic.evaluator.expressions;

import jamsesso.jsonlogic.ast.JsonLogicNode;
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
  public Object evaluate(JsonLogicEvaluator evaluator, JsonLogicNode argument, Object data)
    throws JsonLogicEvaluationException {
    boolean result = (boolean) delegate.evaluate(evaluator, argument, data);

    return !result;
  }
}
