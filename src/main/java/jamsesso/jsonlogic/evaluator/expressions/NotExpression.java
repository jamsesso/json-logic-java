package jamsesso.jsonlogic.evaluator.expressions;

import jamsesso.jsonlogic.JsonLogic;

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
    return isDoubleBang ? "!!" : "!";
  }

  @Override
  public Object evaluate(List arguments, Object data) {
    boolean result;

    if (arguments.isEmpty()) {
      result = false;
    }
    else {
      result = JsonLogic.truthy(arguments.get(0));
    }

    if (isDoubleBang) {
      return result;
    }

    return !result;
  }
}
