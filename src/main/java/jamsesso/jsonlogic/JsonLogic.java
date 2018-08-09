package jamsesso.jsonlogic;

import jamsesso.jsonlogic.ast.JsonLogicNode;
import jamsesso.jsonlogic.ast.JsonLogicParser;
import jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;

public final class JsonLogic {
  private static final JsonLogicEvaluator DEFAULT_EVALUATOR = new JsonLogicEvaluator();

  private final String json;
  private final JsonLogicEvaluator evaluator;
  private JsonLogicNode parseCache;

  public JsonLogic(String json) {
    this(json, DEFAULT_EVALUATOR);
  }

  public JsonLogic(String json, JsonLogicEvaluator evaluator) {
    this.json = json;
    this.evaluator = evaluator;
  }

  public Object apply(Object data) throws JsonLogicException {
    if (parseCache == null) {
      parseCache = JsonLogicParser.parse(json);
    }

    return evaluator.evaluate(parseCache, data);
  }

  public static Object apply(String json, Object data) throws JsonLogicException {
    JsonLogic jsonLogic = new JsonLogic(json);
    return jsonLogic.apply(data);
  }
}
