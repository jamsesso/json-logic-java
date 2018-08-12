package jamsesso.jsonlogic.ast;

public class JsonLogicVariable implements JsonLogicNode {
  private final JsonLogicPrimitive<?> key;
  private final JsonLogicNode defaultValue;

  public JsonLogicVariable(JsonLogicPrimitive<?> key, JsonLogicNode defaultValue) {
    this.key = key;
    this.defaultValue = defaultValue;
  }

  @Override
  public JsonLogicNodeType getType() {
    return JsonLogicNodeType.VARIABLE;
  }

  public JsonLogicPrimitive<?> getKey() {
    return key;
  }

  public JsonLogicNode getDefaultValue() {
    return defaultValue;
  }
}
