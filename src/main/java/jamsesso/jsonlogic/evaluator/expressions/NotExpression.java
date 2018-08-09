package jamsesso.jsonlogic.evaluator.expressions;

import jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;

import java.util.List;

public class NotExpression implements PreEvaluatedArgumentsExpression {
  public static final NotExpression SINGLE = new NotExpression(false);
  public static final NotExpression DOUBLE = new NotExpression(true);

  private final boolean isDoubleBang;

  private NotExpression(boolean isDoubleBang) {
    this.isDoubleBang = isDoubleBang;
  }

  @Override
  public String key() {
    return "!";
  }

  @Override
  public Object evaluate(Object argument, Object data) throws JsonLogicEvaluationException {
    if (argument instanceof List) {
      List arguments = (List) argument;

      if (arguments.size() != 1) {
        throw new JsonLogicEvaluationException("not operator (!) expects exactly one argument");
      }

      argument = arguments.get(0);
    }

    boolean result = JsonLogicEvaluator.isTruthy(argument);

    if (isDoubleBang) {
      return result;
    }

    return !result;
  }
}
