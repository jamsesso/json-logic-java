package io.github.jamsesso.jsonlogic.evaluator;

import io.github.jamsesso.jsonlogic.ast.*;
import io.github.jamsesso.jsonlogic.utils.ArrayLike;

import java.lang.reflect.Array;
import java.util.*;

public class JsonLogicEvaluator {

  /** Sentinel object to represent a missing value (for internal use only). */
  private static final Object MISSING = new Object();

  private final Map<String, JsonLogicExpression> expressions;

  public JsonLogicEvaluator(Collection<JsonLogicExpression> expressions) {
    this.expressions = new HashMap<>();

    for (JsonLogicExpression expression : expressions) {
      this.expressions.put(expression.key(), expression);
    }
  }

  public JsonLogicEvaluator(Map<String, JsonLogicExpression> expressions) {
    this.expressions = Collections.unmodifiableMap(expressions);
  }

  public Object evaluate(JsonLogicNode node, Object data, String jsonPath) throws JsonLogicEvaluationException {
    switch (node.getType()) {
      case PRIMITIVE: return evaluate((JsonLogicPrimitive) node);
      case VARIABLE: return evaluate((JsonLogicVariable) node, data, jsonPath + ".var");
      case ARRAY: return evaluate((JsonLogicArray) node, data, jsonPath);
      default: return evaluate((JsonLogicOperation) node, data, jsonPath);
    }
  }

  public Object evaluate(JsonLogicPrimitive<?> primitive) {
    switch (primitive.getPrimitiveType()) {
      case NUMBER: return ((JsonLogicNumber) primitive).getValue();

      default:
        return primitive.getValue();
    }
  }

  public Object evaluate(JsonLogicVariable variable, Object data, String jsonPath)
      throws JsonLogicEvaluationException {
    Object defaultValue = evaluate(variable.getDefaultValue(), null, jsonPath + "[1]");

    if (data == null) {
      return defaultValue;
    }

    Object key = evaluate(variable.getKey(), data, jsonPath + "[0]");

    if (key == null) {
      return Optional.of(data)
        .map(JsonLogicEvaluator::transform)
        .orElse(evaluate(variable.getDefaultValue(), null, jsonPath + "[1]"));
    }

    if (key instanceof Number) {
      int index = ((Number) key).intValue();

      if (ArrayLike.isEligible(data)) {
        Object item = indexedValue(data, index);
        return item == MISSING ? defaultValue : item;
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

      for (String partial : keys) {
        result = evaluatePartialVariable(partial, result, jsonPath + "[0]");

        if (result == MISSING) {
          return defaultValue;
        } else if (result == null) {
          return null;
        }
      }

      return result;
    }

    throw new JsonLogicEvaluationException("var first argument must be null, number, or string", jsonPath + "[0]");
  }

  private static Object indexedValue(Object data, int index) {
    if (data.getClass().isArray()) {
      if (index < 0 || index >= Array.getLength(data)) {
        return MISSING;
      }
      return transform(Array.get(data, index));
    }

    List<?> values;
    if (data instanceof List<?> && data instanceof RandomAccess) {
      values = (List<?>) data;
    } else {
      values = new ArrayLike(data);
    }

    if (index < 0 || index >= values.size()) {
      return MISSING;
    }
    return transform(values.get(index));
  }

  private Object evaluatePartialVariable(String key, Object data, String jsonPath) throws JsonLogicEvaluationException {
    if (ArrayLike.isEligible(data)) {
      int index;

      try {
        index = Integer.parseInt(key);
      }
      catch (NumberFormatException e) {
        throw new JsonLogicEvaluationException(e, jsonPath);
      }

      return indexedValue(data, index);
    }

    if (data instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) data;
      if (map.containsKey(key)) {
        return transform(map.get(key));
      } else {
        return MISSING;
      }
    }

    return null;
  }

  public List<Object> evaluate(JsonLogicArray array, Object data, String jsonPath) throws JsonLogicEvaluationException {
    List<Object> values = new ArrayList<>(array.size());

    int index = 0;
    for(JsonLogicNode element : array) {
      values.add(evaluate(element, data, String.format("%s[%d]", jsonPath, index++)));
    }

    return values;
  }

  public Object evaluate(JsonLogicOperation operation, Object data, String jsonPath) throws JsonLogicEvaluationException {
    JsonLogicExpression handler = expressions.get(operation.getOperator());

    if (handler == null) {
      throw new JsonLogicEvaluationException("Undefined operation '" + operation.getOperator() + "'", jsonPath);
    }

    return handler.evaluate(this, operation.getArguments(), data, String.format("%s.%s", jsonPath, operation.getOperator()));
  }

  public static Object transform(Object value) {
    if (value instanceof Number) {
      return ((Number) value).doubleValue();
    }

    return value;
  }
}
