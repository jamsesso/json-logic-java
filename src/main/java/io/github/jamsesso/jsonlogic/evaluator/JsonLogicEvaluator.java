package io.github.jamsesso.jsonlogic.evaluator;

import io.github.jamsesso.jsonlogic.JsonLogicException;
import io.github.jamsesso.jsonlogic.ast.*;
import io.github.jamsesso.jsonlogic.utils.ArrayLike;

import java.util.*;

public class JsonLogicEvaluator {

  /**
   * Sentinel object to represent a missing value (for internal use only).
   */
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

  public static Object transform(Object value) {
    if (value instanceof Number) {
      return ((Number) value).doubleValue();
    }

    return value;
  }

  @Deprecated
  public Object evaluate(JsonLogicNode node, Object data, String jsonPath) throws JsonLogicEvaluationException {
    try {
      return evaluate(node, data);
    } catch (JsonLogicEvaluationException e) {
      e.prependPartialJsonPath(jsonPath);
      throw e;
    }
  }

  public Object evaluate(JsonLogicNode node, Object data) throws JsonLogicEvaluationException {
    switch (node.getType()) {
      case PRIMITIVE:
        return evaluate((JsonLogicPrimitive) node);
      case VARIABLE:
        try {
          return evaluate((JsonLogicVariable) node, data);
        } catch (JsonLogicEvaluationException e) {
          e.prependPartialJsonPath(".var");
          throw e;
        }
      case ARRAY:
        return evaluate((JsonLogicArray) node, data);
      default:
        return evaluate((JsonLogicOperation) node, data);
    }
  }

  public Object evaluate(JsonLogicPrimitive<?> primitive) {
    switch (primitive.getPrimitiveType()) {
      case NUMBER:
        return ((JsonLogicNumber) primitive).getValue();

      default:
        return primitive.getValue();
    }
  }

  @Deprecated
  public Object evaluate(JsonLogicVariable variable, Object data, String jsonPath)
      throws JsonLogicEvaluationException {
    try {
      return evaluate(variable, data);
    } catch (JsonLogicEvaluationException e) {
      e.prependPartialJsonPath(jsonPath);
      throw e;
    }
  }

  public Object evaluate(JsonLogicVariable variable, Object data)
      throws JsonLogicEvaluationException {
    Object defaultValue;

    try {
      defaultValue = evaluate(variable.getDefaultValue(), null);
    } catch (JsonLogicEvaluationException e) {
      e.prependPartialJsonPath("[1]");
      throw e;
    }

    if (data == null) {
      return defaultValue;
    }

    Object key;

    try {
      key = evaluate(variable.getKey(), data);
    } catch (JsonLogicEvaluationException e) {
      e.prependPartialJsonPath("[0]");
      throw e;
    }

    if (key == null) {
      Object varValue;
      try {
        varValue = evaluate(variable.getDefaultValue(), null);
      } catch (JsonLogicEvaluationException e) {
        e.prependPartialJsonPath("[0]");
        throw e;
      }

      return Optional.of(data)
          .map(JsonLogicEvaluator::transform)
          .orElse(varValue);
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

      for (String partial : keys) {
        try {
          result = evaluatePartialVariable(partial, result);
        } catch (JsonLogicEvaluationException e) {
          e.prependPartialJsonPath("[0]");
          throw e;
        }

        if (result == MISSING) {
          return defaultValue;
        } else if (result == null) {
          return null;
        }
      }

      return result;
    }

    throw new JsonLogicEvaluationException("var first argument must be null, number, or string", "[0]");
  }

  private Object evaluatePartialVariable(String key, Object data) throws JsonLogicEvaluationException {
    if (ArrayLike.isEligible(data)) {
      ArrayLike list = new ArrayLike(data);
      int index;

      try {
        index = Integer.parseInt(key);
      } catch (NumberFormatException e) {
        throw new JsonLogicEvaluationException(e);
      }

      if (index < 0 || index >= list.size()) {
        return MISSING;
      }

      return transform(list.get(index));
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

  @Deprecated
  public List<Object> evaluate(JsonLogicArray array, Object data, String jsonPath) throws JsonLogicEvaluationException {
    try {
      return evaluate(array, data);
    } catch (JsonLogicEvaluationException e) {
      e.prependPartialJsonPath(jsonPath);
      throw e;
    }
  }

  public List<Object> evaluate(JsonLogicArray array, Object data) throws JsonLogicEvaluationException {
    List<Object> values = new ArrayList<>(array.size());

    for (int index = 0; index < array.size(); index++) {
      JsonLogicNode element = array.get(index);
      try {
        values.add(evaluate(element, data));
      } catch (JsonLogicEvaluationException e) {
        e.prependPartialJsonPath("[" + index + "]");
        throw e;
      }
    }

    return values;
  }

  @Deprecated
  public Object evaluate(JsonLogicOperation operation, Object data, String jsonPath) throws JsonLogicEvaluationException {
    try {
      return evaluate(operation, data);
    } catch (JsonLogicEvaluationException e) {
      e.prependPartialJsonPath(jsonPath);
      throw e;
    }
  }

  public Object evaluate(JsonLogicOperation operation, Object data) throws JsonLogicEvaluationException {
    JsonLogicExpression handler = expressions.get(operation.getOperator());

    if (handler == null) {
      throw new JsonLogicEvaluationException("Undefined operation '" + operation.getOperator() + "'");
    }

    try {
      return handler.evaluate(this, operation.getArguments(), data, "");
    } catch (JsonLogicException e) {
      e.prependPartialJsonPath("." + operation.getOperator());
      throw e;
    }
  }
}
