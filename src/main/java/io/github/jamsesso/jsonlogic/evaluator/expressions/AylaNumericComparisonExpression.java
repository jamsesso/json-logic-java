package io.github.jamsesso.jsonlogic.evaluator.expressions;

import io.github.jamsesso.jsonlogic.ast.JsonLogicArray;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicExpression;

public class AylaNumericComparisonExpression implements JsonLogicExpression {

  public static final AylaNumericComparisonExpression GT = new AylaNumericComparisonExpression("gt");
  public static final AylaNumericComparisonExpression GE = new AylaNumericComparisonExpression("ge");
  public static final AylaNumericComparisonExpression LT = new AylaNumericComparisonExpression("lt");
  public static final AylaNumericComparisonExpression LE = new AylaNumericComparisonExpression("le");
  public static final AylaNumericComparisonExpression EQ = new AylaNumericComparisonExpression("eq");
  public static final AylaNumericComparisonExpression NE = new AylaNumericComparisonExpression("ne");

  private final String operator;

  private AylaNumericComparisonExpression(String operator) {
    this.operator = operator;
  }

  @Override
  public String key() {
    return operator;
  }

  @Override
  public Object evaluate(JsonLogicEvaluator evaluator, JsonLogicArray arguments, Object data)
    throws JsonLogicEvaluationException {
    return new JsonLogicEvaluationException("not implemented yet");
  }
}
