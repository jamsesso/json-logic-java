package jamsesso.jsonlogic.evaluator.expressions;

import jamsesso.jsonlogic.JsonLogic;
import jamsesso.jsonlogic.ast.JsonLogicArray;
import jamsesso.jsonlogic.ast.JsonLogicNode;
import jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import jamsesso.jsonlogic.evaluator.JsonLogicExpression;

public class IfExpression implements JsonLogicExpression {
  public static final IfExpression IF = new IfExpression("if");
  public static final IfExpression TERNARY = new IfExpression("?:");

  private final String operator;

  private IfExpression(String operator) {
    this.operator = operator;
  }

  @Override
  public String key() {
    return operator;
  }

  @Override
  public Object evaluate(JsonLogicEvaluator evaluator, JsonLogicArray arguments, Object data)
    throws JsonLogicEvaluationException {
    // There must be at least 3 arguments
    if (arguments.size() < 3 || arguments.size() % 2 != 1) {
      return null;
    }

    for (int i = 0; i < arguments.size() - 1; i += 2) {
      JsonLogicNode condition = arguments.get(i);
      JsonLogicNode resultIfTrue = arguments.get(i + 1);

      if (JsonLogic.truthy(evaluator.evaluate(condition, data))) {
        return evaluator.evaluate(resultIfTrue, data);
      }
    }

    return evaluator.evaluate(arguments.get(arguments.size() - 1), data);
  }
}
