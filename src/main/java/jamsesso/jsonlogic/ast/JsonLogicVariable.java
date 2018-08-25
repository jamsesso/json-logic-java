package jamsesso.jsonlogic.ast;

public class JsonLogicVariable implements JsonLogicNode {
  private final JsonLogicNode key;
  private final JsonLogicNode defaultValue;

  public JsonLogicVariable(JsonLogicNode key, JsonLogicNode defaultValue) {
    this.key = key;
    this.defaultValue = defaultValue;
  }

  @Override
  public JsonLogicNodeType getType() {
    return JsonLogicNodeType.VARIABLE;
  }

  public JsonLogicNode getKey() {
    return key;
  }

  public JsonLogicNode getDefaultValue() {
    return defaultValue;
  }
}
