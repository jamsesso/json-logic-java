package io.github.jamsesso.jsonlogic.ast;

import java.math.BigDecimal;

public class JsonLogicNumber implements JsonLogicPrimitive<BigDecimal> {
  private final BigDecimal value;

  public JsonLogicNumber(BigDecimal value) {
    this.value = value;
  }

  @Override
  public BigDecimal getValue() {
    return value;
  }

  @Override
  public JsonLogicPrimitiveType getPrimitiveType() {
    return JsonLogicPrimitiveType.NUMBER;
  }
}
