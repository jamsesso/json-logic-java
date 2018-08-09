package jamsesso.jsonlogic.ast;

public class JsonLogicOperation implements JsonLogicNode {
  private final String operator;
  private final JsonLogicNode argument;

  public JsonLogicOperation(String operator, JsonLogicNode argument) {
    this.operator = operator;
    this.argument = argument;
  }

  @Override
  public JsonLogicNodeType getType() {
    return JsonLogicNodeType.OPERATION;
  }

  public String getOperator() {
    return operator;
  }

  public JsonLogicNode getArgument() {
    return argument;
  }
}
