package io.github.jamsesso.jsonlogic.evaluator;

import io.github.jamsesso.jsonlogic.ast.*;
import io.github.jamsesso.jsonlogic.utils.ArrayLike;
import io.github.jamsesso.jsonlogic.utils.BigDecimalOperations;

import java.util.*;

public class JsonLogicEvaluator {
  private final Map<String, JsonLogicExpression> expressions = new HashMap<>();

  public JsonLogicEvaluator(Collection<JsonLogicExpression> expressions) {
    for (JsonLogicExpression expression : expressions) {
      this.expressions.put(expression.key(), expression);
    }
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
      case NUMBER: return ((JsonLogicNumber) primitive).getValue();

      default:
        return primitive.getValue();
    }
  }

  public Object evaluate(JsonLogicVariable variable, Object data) throws JsonLogicEvaluationException {
    Object defaultValue = evaluate(variable.getDefaultValue(), null);

    if (data == null) {
      return defaultValue;
    }

    Object key = evaluate(variable.getKey(), data);

    if (key == null) {
      return Optional.of(data)
        .map(JsonLogicEvaluator::transform)
        .orElse(evaluate(variable.getDefaultValue(), null));
    }

    if (key instanceof Number) {
      int index = ((Number) key).intValue();

      if (ArrayLike.isEligible(data)) {
        ArrayLike list = new ArrayLike(data);

        if (index >= 0 && index < list.size()) {
          return transform(list.get(index));
        }
      }

      return defaultValue;
    }

    // Handle the case when the key is a string, potentially referencing an infinitely-deep map: x.y.z
    if (key instanceof String) {
      String name = (String) key;

      if (name.isEmpty()) {
        return data;
      }

      String[] keys = name.split("\\.");
      Object result = data;

      for(String partial : keys) {
        result = evaluatePartialVariable(partial, result);

        if(result == null) {
          return defaultValue;
        }
      }

      return result;
    }

    throw new JsonLogicEvaluationException("var first argument must be null, number, or string");
  }

  private Object evaluatePartialVariable(String key, Object data) throws JsonLogicEvaluationException {
    if (ArrayLike.isEligible(data)) {
      ArrayLike list = new ArrayLike(data);
      int index;

      try {
        index = Integer.parseInt(key);
      }
      catch (NumberFormatException e) {
        throw new JsonLogicEvaluationException(e);
      }

      if (index < 0 || index >= list.size()) {
        return null;
      }

      return transform(list.get(index));
    }

    if (data instanceof Map) {
      return transform(((Map) data).get(key));
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

  public static Object transform(Object value) {
    if (value instanceof Number) {
      return BigDecimalOperations.fromNumber((Number) value);
    }

    return value;
  }
}
