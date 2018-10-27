package io.github.jamsesso.jsonlogic.ast;

public class JsonLogicNumber implements JsonLogicPrimitive<Number> {
  private final Number value;

  public JsonLogicNumber(Number value) {
    this.value = value;
  }

  @Override
  public Number getValue() {
    return value;
  }

  @Override
  public JsonLogicPrimitiveType getPrimitiveType() {
    return JsonLogicPrimitiveType.NUMBER;
  }
}
