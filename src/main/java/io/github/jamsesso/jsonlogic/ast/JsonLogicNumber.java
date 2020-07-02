package io.github.jamsesso.jsonlogic.ast;

public class JsonLogicNumber implements JsonLogicPrimitive<Double> {
  private final Number value;

  public JsonLogicNumber(Number value) {
    this.value = value;
  }

  @Override
  public Double getValue() {
    return value.doubleValue();
  }

  @Override
  public JsonLogicPrimitiveType getPrimitiveType() {
    return JsonLogicPrimitiveType.NUMBER;
  }
}
