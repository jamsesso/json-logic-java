package jamsesso.jsonlogic.evaluator;

import jamsesso.jsonlogic.ast.*;
import jamsesso.jsonlogic.evaluator.expressions.*;

import java.util.*;

public class JsonLogicEvaluator {
  private final Map<String, JsonLogicExpression> expressions = new HashMap<>();

  public JsonLogicEvaluator() {
    extend(MathExpression.ADD);
    extend(MathExpression.SUBTRACT);
    extend(MathExpression.MULTIPLY);
    extend(MathExpression.DIVIDE);
    extend(MathExpression.MODULO);
    extend(MathExpression.MIN);
    extend(MathExpression.MAX);
    extend(NumericComparisonExpression.GT);
    extend(NumericComparisonExpression.GTE);
    extend(NumericComparisonExpression.LT);
    extend(NumericComparisonExpression.LTE);
    extend(IfExpression.INSTANCE);
    extend(EqualityExpression.INSTANCE);
    extend(InequalityExpression.INSTANCE);
    extend(StrictEqualityExpression.INSTANCE);
    extend(StrictInequalityExpression.INSTANCE);
    extend(NotExpression.SINGLE);
    extend(NotExpression.DOUBLE);
    extend(LogicExpression.AND);
    extend(LogicExpression.OR);
    extend(LogExpression.STDOUT);
  }

  public JsonLogicEvaluator extend(JsonLogicExpression expression) {
    expressions.put(expression.key().toLowerCase(), expression);
    return this;
  }

  public Object evaluate(JsonLogicNode node, Object data) throws JsonLogicEvaluationException {
    switch (node.getType()) {
      case PRIMITIVE: return evaluate((JsonLogicPrimitive) node);
      case VARIABLE: return evaluate((JsonLogicVariable) node, data);
      case ARRAY: return evaluate((JsonLogicArray) node, data);
      default: return evaluate((JsonLogicOperation) node, data);
    }
  }

  public Object evaluate(JsonLogicPrimitive<?> primitive) {
    return primitive.getValue();
  }

  public Object evaluate(JsonLogicVariable variable, Object data) {
    return variable.resolve(data);
  }

  public List<Object> evaluate(JsonLogicArray array, Object data) throws JsonLogicEvaluationException {
    List<Object> values = new ArrayList<>(array.size());

    for(JsonLogicNode element : array) {
      values.add(evaluate(element, data));
    }

    return values;
  }

  public Object evaluate(JsonLogicOperation operation, Object data) throws JsonLogicEvaluationException {
    JsonLogicExpression handler = expressions.get(operation.getOperator());

    if (handler == null) {
      throw new JsonLogicEvaluationException("Undefined operation '" + operation.getOperator() + "'");
    }

    return handler.evaluate(this, operation.getArguments(), data);
  }
}
