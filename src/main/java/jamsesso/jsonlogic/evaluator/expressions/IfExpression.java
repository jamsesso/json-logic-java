package jamsesso.jsonlogic.evaluator.expressions;

import jamsesso.jsonlogic.JsonLogic;
import jamsesso.jsonlogic.ast.JsonLogicArray;
import jamsesso.jsonlogic.ast.JsonLogicNode;
import jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import jamsesso.jsonlogic.evaluator.JsonLogicExpression;

public class IfExpression implements JsonLogicExpression {
  public static final IfExpression INSTANCE = new IfExpression();

  private IfExpression() {
    // Only one instance can be constructed. Use IfExpression.INSTANCE
  }

  @Override
  public String key() {
    return "if";
  }

  @Override
  public Object evaluate(JsonLogicEvaluator evaluator, JsonLogicArray arguments, Object data)
    throws JsonLogicEvaluationException {
    // There must be at least 3 arguments
    if (arguments.size() < 3 || arguments.size() % 2 != 1) {
      throw new JsonLogicEvaluationException("if expressions expect an odd number of arguments, minimum of 3");
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
