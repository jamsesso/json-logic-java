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
  }

  public JsonLogicEvaluator extend(JsonLogicExpression expression) {
    expressions.put(expression.key().toLowerCase(), expression);
    return this;
  }

  public Object evaluate(JsonLogicNode node, Object data) throws JsonLogicEvaluationException {
    switch (node.getType()) {
      // Evaluate primitive values
      case PRIMITIVE: {
        JsonLogicPrimitive<?> primitive = (JsonLogicPrimitive) node;
        return primitive.getValue();
      }

      // Evaluate variables
      case VARIABLE: {
        JsonLogicVariable variable = (JsonLogicVariable) node;
        return variable.resolve(data);
      }

      // Evaluate arrays
      case ARRAY: {
        JsonLogicArray array = (JsonLogicArray) node;
        List<Object> values = new ArrayList<>(array.size());

        for(JsonLogicNode element : array) {
          values.add(evaluate(element, data));
        }

        return values;
      }

      // Evaluate objects
      default: {
        JsonLogicOperation operation = (JsonLogicOperation) node;
        JsonLogicExpression handler = expressions.get(operation.getOperator());

        if (handler == null) {
          throw new JsonLogicEvaluationException("Undefined operation '" + operation.getOperator() + "'");
        }

        return handler.evaluate(this, operation.getArgument(), data);
      }
    }
  }

  public static boolean isTruthy(Object value) {
    if (value == null) {
      return false;
    }

    if (value instanceof Boolean) {
      return (boolean) value;
    }

    if (value instanceof Number) {
      return ((Number) value).doubleValue() != 0.0;
    }

    if (value instanceof String) {
      return !((String) value).isEmpty();
    }

    if (value instanceof Collection) {
      return !((Collection) value).isEmpty();
    }

    return true;
  }
}
