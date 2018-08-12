package jamsesso.jsonlogic.evaluator;

import jamsesso.jsonlogic.ast.*;
import jamsesso.jsonlogic.evaluator.expressions.*;
import jamsesso.jsonlogic.utils.IndexedStructure;

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
    extend(MapExpression.INSTANCE);
    extend(FilterExpression.INSTANCE);
    extend(ReduceExpression.INSTANCE);
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
    switch (primitive.getPrimitiveType()) {
      case NUMBER: return ((JsonLogicNumber) primitive).getValue().doubleValue();

      default:
        return primitive.getValue();
    }
  }

  public Object evaluate(JsonLogicVariable variable, Object data) throws JsonLogicEvaluationException {
    Object defaultValue = evaluate(variable.getDefaultValue(), null);

    if (data == null) {
      return defaultValue;
    }

    switch (variable.getKey().getPrimitiveType()) {
      // Handle if the first argument is null.
      case NULL: return evaluate(variable.getDefaultValue(), null);

      // Handle the case when the key is a number - in this case, the data must be an array or list.
      case NUMBER: {
        int key = ((JsonLogicNumber) variable.getKey()).getValue().intValue();

        if (IndexedStructure.isEligible(data)) {
          IndexedStructure list = new IndexedStructure(data);

          if (key >= 0 && key < list.size()) {
            return list.get(key);
          }
        }

        return defaultValue;
      }

      // Handle the case when the key is a string, potentially referencing an infinitely-deep map: x.y.z
      case STRING: {
        String key = ((JsonLogicString) variable.getKey()).getValue();

        if (key.isEmpty()) {
          return data;
        }

        String[] keys = key.split("\\.");
        Object result = data;

        for (int i = 0; i < keys.length; i++) {
          result = evaluatePartialVariable(keys[i], result);

          if (result == null) {
            return defaultValue;
          }
        }

        return result;
      }

      default:
        throw new JsonLogicEvaluationException("var first argument must be null, number, or string");
    }
  }

  private Object evaluatePartialVariable(String key, Object data) throws JsonLogicEvaluationException {
    if (IndexedStructure.isEligible(data)) {
      IndexedStructure list = new IndexedStructure(data);
      int index;

      try {
        index = Integer.parseInt(key);
      }
      catch (NumberFormatException e) {
        throw new JsonLogicEvaluationException(e);
      }

      if (index < 0 || index > list.size()) {
        return null;
      }

      return list.get(index);
    }

    if (data instanceof Map) {
      return ((Map) data).get(key);
    }

    return null;
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
