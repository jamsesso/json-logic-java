package io.github.jamsesso.jsonlogic;

import io.github.jamsesso.jsonlogic.ast.JsonLogicNode;
import io.github.jamsesso.jsonlogic.ast.JsonLogicParser;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicExpression;
import io.github.jamsesso.jsonlogic.evaluator.expressions.*;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

public final class JsonLogic {
  private final List<JsonLogicExpression> expressions;
  private final Map<String, JsonLogicNode> parseCache;
  private JsonLogicEvaluator evaluator;

  public JsonLogic() {
    this.expressions = new ArrayList<>();
    this.parseCache = new ConcurrentHashMap<>();

    // Add default operations
    addOperation(MathExpression.ADD);
    addOperation(MathExpression.SUBTRACT);
    addOperation(MathExpression.MULTIPLY);
    addOperation(MathExpression.DIVIDE);
    addOperation(MathExpression.MODULO);
    addOperation(MathExpression.MIN);
    addOperation(MathExpression.MAX);
    addOperation(NumericComparisonExpression.GT);
    addOperation(NumericComparisonExpression.GTE);
    addOperation(NumericComparisonExpression.LT);
    addOperation(NumericComparisonExpression.LTE);
    addOperation(IfExpression.IF);
    addOperation(IfExpression.TERNARY);
    addOperation(EqualityExpression.INSTANCE);
    addOperation(InequalityExpression.INSTANCE);
    addOperation(StrictEqualityExpression.INSTANCE);
    addOperation(StrictInequalityExpression.INSTANCE);
    addOperation(NotExpression.SINGLE);
    addOperation(NotExpression.DOUBLE);
    addOperation(LogicExpression.AND);
    addOperation(LogicExpression.OR);
    addOperation(LogExpression.STDOUT);
    addOperation(MapExpression.INSTANCE);
    addOperation(FilterExpression.INSTANCE);
    addOperation(ReduceExpression.INSTANCE);
    addOperation(AllExpression.INSTANCE);
    addOperation(ArrayHasExpression.SOME);
    addOperation(ArrayHasExpression.NONE);
    addOperation(MergeExpression.INSTANCE);
    addOperation(InExpression.INSTANCE);
    addOperation(ConcatenateExpression.INSTANCE);
    addOperation(SubstringExpression.INSTANCE);
    addOperation(MissingExpression.ALL);
    addOperation(MissingExpression.SOME);
  }

  public JsonLogic addOperation(String name, Function<Object[], Object> function) {
    return addOperation(new PreEvaluatedArgumentsExpression() {
      @Override
      public Object evaluate(List arguments, Object data) {
        return function.apply(arguments.toArray());
      }

      @Override
      public String key() {
        return name;
      }
    });
  }

  public JsonLogic addOperation(JsonLogicExpression expression) {
    expressions.add(expression);
    evaluator = null;

    return this;
  }

  public Object apply(String json, Object data) throws JsonLogicException {
    if (!parseCache.containsKey(json)) {
      parseCache.put(json, JsonLogicParser.parse(json));
    }

    if (evaluator == null) {
      evaluator = new JsonLogicEvaluator(expressions);
    }

    return evaluator.evaluate(parseCache.get(json), data);
  }

  public static boolean truthy(Object value) {
    if (value == null) {
      return false;
    }

    if (value instanceof Boolean) {
      return (boolean) value;
    }

    if (value instanceof Number) {
      if (value instanceof BigDecimal) {
        return ((BigDecimal) value).compareTo(BigDecimal.ZERO) != 0;
      }

      if (value instanceof Double) {
        Double d = (Double) value;

        if (d.isNaN()) {
          return false;
        }
        else if (d.isInfinite()) {
          return true;
        }
      }

      if (value instanceof Float) {
        Float f = (Float) value;

        if (f.isNaN()) {
          return false;
        }
        else if (f.isInfinite()) {
          return true;
        }
      }

      return ((Number) value).doubleValue() != 0.0;
    }

    if (value instanceof String) {
      return !((String) value).isEmpty();
    }

    if (value instanceof Collection) {
      return !((Collection) value).isEmpty();
    }

    if (value.getClass().isArray()) {
      return Array.getLength(value) > 0;
    }

    return true;
  }
}
