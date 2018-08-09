package jamsesso.jsonlogic.ast;

public class JsonLogicString implements JsonLogicPrimitive<String> {
  private final String value;

  public JsonLogicString(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public JsonLogicPrimitiveType getPrimitiveType() {
    return JsonLogicPrimitiveType.STRING;
  }
}
