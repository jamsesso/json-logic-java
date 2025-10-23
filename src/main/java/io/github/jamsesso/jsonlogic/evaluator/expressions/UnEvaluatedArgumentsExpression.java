package io.github.jamsesso.jsonlogic.evaluator.expressions;

import io.github.jamsesso.jsonlogic.ast.JsonLogicArray;
import io.github.jamsesso.jsonlogic.evaluator.*;

/**
 * This class represents an unevaluated arguments expression that uses a macro to delay
 * the evaluation of arguments in a specific JsonLogic context. The expression defers the
 * responsibility of argument evaluation to the provided macro.
 * <p>
 * This class implements the {@link JsonLogicExpression} interface, enabling it to be
 * used as a type of expression within the JsonLogic evaluation framework.
 */
public class UnEvaluatedArgumentsExpression implements JsonLogicExpression {
  private final String operator;
  private final Macro macro;

  public UnEvaluatedArgumentsExpression(String operator,
                                        Macro macro) {
    this.operator = operator;
      this.macro = macro;
  }

  @Override
  public String key() {
    return operator;
  }

  @Override
  public Object evaluate(JsonLogicEvaluator evaluator, JsonLogicArray arguments, Object data,
      String jsonPath)
    throws JsonLogicEvaluationException {
    return macro.evaluate(new MacroArguments(evaluator,arguments,data,jsonPath));
  }
}
