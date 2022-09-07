package io.github.jamsesso.jsonlogic.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalOperations {
    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return a.add(b);
    }
    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return a.subtract(b);
    }
    public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
        return a.multiply(b);
    }
    public static BigDecimal divide(BigDecimal a, BigDecimal b) {
        return a.divide(b, 4, RoundingMode.HALF_UP);
    }
    public static BigDecimal modulo(BigDecimal a, BigDecimal b) {
        return a.remainder(b);
    }
    public static BigDecimal min(BigDecimal a, BigDecimal b) {
        return a.min(b);
    }
    public static BigDecimal max(BigDecimal a, BigDecimal b) {
        return a.max(b);
    }
    public static BigDecimal fromNumber(Number n) {
        return new BigDecimal(n.toString());
    }
}
