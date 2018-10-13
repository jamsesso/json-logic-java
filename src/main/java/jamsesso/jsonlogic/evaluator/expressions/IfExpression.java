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
    if (arguments.size() < 1) {
      return null;
    }

    // If there is only a single argument, simply evaluate & return that argument.
    if (arguments.size() == 1) {
      return evaluator.evaluate(arguments.get(0), data);
    }

    // If there is 2 arguments, only evaluate the second argument if the first argument is truthy.
    if (arguments.size() == 2) {
      return JsonLogic.truthy(evaluator.evaluate(arguments.get(0), data))
        ? evaluator.evaluate(arguments.get(1), data)
        : null;
    }

    for (int i = 0; i < arguments.size() - 1; i += 2) {
      JsonLogicNode condition = arguments.get(i);
      JsonLogicNode resultIfTrue = arguments.get(i + 1);

      if (JsonLogic.truthy(evaluator.evaluate(condition, data))) {
        return evaluator.evaluate(resultIfTrue, data);
      }
    }

    if ((arguments.size() & 1) == 0) {
      return null;
    }

    return evaluator.evaluate(arguments.get(arguments.size() - 1), data);
  }
}
