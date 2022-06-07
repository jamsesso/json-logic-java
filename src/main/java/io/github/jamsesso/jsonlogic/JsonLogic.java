package io.github.jamsesso.jsonlogic;

import io.github.jamsesso.jsonlogic.ast.JsonLogicNode;
import io.github.jamsesso.jsonlogic.ast.JsonLogicParser;
import io.github.jamsesso.jsonlogic.cache.ConcurrentHashMapCache;
import io.github.jamsesso.jsonlogic.cache.JsonLogicCache;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicExpression;
import io.github.jamsesso.jsonlogic.evaluator.expressions.*;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public final class JsonLogic {
  private final List<JsonLogicExpression> expressions;
  private JsonLogicCache parseCache;
  private JsonLogicEvaluator evaluator;

  public JsonLogic() {
    this.expressions = new ArrayList<>();

    addCache(new ConcurrentHashMapCache());

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
  public JsonLogic addCache(JsonLogicCache cache) {
    parseCache = cache;
    return this;
  }
  public List<JsonLogicExpression> getExpressions() {
    return expressions;
  }

  public Object apply(String json, Object data) throws JsonLogicException {
    JsonLogicNode ast;

    if (parseCache.containsKey(json)) {
      ast = parseCache.get(json);
    } else {
      ast = JsonLogicParser.parse(json);
      parseCache.put(json, ast);
    }

    if (evaluator == null) {
      evaluator = new JsonLogicEvaluator(expressions);
    }

    return evaluator.evaluate(ast, data);
  }

  public static boolean truthy(Object value) {
    if (value == null) {
      return false;
    }

    if (value instanceof Boolean) {
      return (boolean) value;
    }

    if (value instanceof Number) {
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
