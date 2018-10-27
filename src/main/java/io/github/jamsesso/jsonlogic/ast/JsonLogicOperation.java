package io.github.jamsesso.jsonlogic.ast;

public class JsonLogicOperation implements JsonLogicNode {
  private final String operator;
  private final JsonLogicArray arguments;

  public JsonLogicOperation(String operator, JsonLogicArray arguments) {
    this.operator = operator;
    this.arguments = arguments;
  }

  @Override
  public JsonLogicNodeType getType() {
    return JsonLogicNodeType.OPERATION;
  }

  public String getOperator() {
    return operator;
  }

  public JsonLogicArray getArguments() {
    return arguments;
  }
}
