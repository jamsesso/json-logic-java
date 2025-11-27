package io.github.jamsesso.jsonlogic.evaluator.expressions;

import io.github.jamsesso.jsonlogic.JsonLogic;
import io.github.jamsesso.jsonlogic.ast.JsonLogicArray;
import io.github.jamsesso.jsonlogic.ast.JsonLogicNode;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicExpression;

public class IfExpression extends JsonPathHandlerJsonLogicExpression implements JsonLogicExpression {
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
      try {
        return evaluator.evaluate(arguments.get(0), data);
      } catch (JsonLogicEvaluationException e) {
        e.prependPartialJsonPath("[0]");
        throw e;
      }
    }

    // If there is 2 arguments, only evaluate the second argument if the first argument is truthy.
    if (arguments.size() == 2) {
      try {
        if (!JsonLogic.truthy(evaluator.evaluate(arguments.get(0), data))) {
          return null;
        }
      } catch (JsonLogicEvaluationException e) {
        e.prependPartialJsonPath("[0]");
        throw e;
      }

      try {
        return evaluator.evaluate(arguments.get(1), data);
      } catch (JsonLogicEvaluationException e) {
        e.prependPartialJsonPath("[1]");
        throw e;
      }
    }

    for (int i = 0; i < arguments.size() - 1; i += 2) {
      JsonLogicNode condition = arguments.get(i);
      JsonLogicNode resultIfTrue = arguments.get(i + 1);

      try {
        if (!JsonLogic.truthy(evaluator.evaluate(condition, data))) {
          continue;
        }
      } catch (JsonLogicEvaluationException e) {
        e.prependPartialJsonPath("[" + i + "]");
        throw e;
      }

      try {
        return evaluator.evaluate(resultIfTrue, data);
      } catch (JsonLogicEvaluationException e) {
        e.prependPartialJsonPath("[" + (i + 1) + "]");
        throw e;
      }

    }

    if ((arguments.size() & 1) == 0) {
      return null;
    }

    try {
      return evaluator.evaluate(arguments.get(arguments.size() - 1), data);
    } catch (JsonLogicEvaluationException e) {
      e.prependPartialJsonPath("[" + (arguments.size() - 1) + "]");
      throw e;
    }
  }
}
