package com.tradin.core.futuresOrder.domain.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Margin {
    private final BigDecimal value;

    private Margin(BigDecimal value) {
        this.value = applyPolicy(value);
    }

    public static Margin of(BigDecimal value) {
        return new Margin(value);
    }

    public BigDecimal getValue() {
        return value;
    }

    private static BigDecimal applyPolicy(BigDecimal value) {
        if (value == null) return null;
        if (value.signum() >= 0) {
            return value.setScale(2, RoundingMode.DOWN);
        } else {
            return value.setScale(2, RoundingMode.UP);
        }
    }

    public Margin add(Margin other) {
        BigDecimal result = this.value.add(other.value);
        return Margin.of(applyPolicy(result));
    }

    public Margin subtract(Margin other) {
        BigDecimal result = this.value.subtract(other.value);
        return Margin.of(applyPolicy(result));
    }

    public boolean isPositive() { return value.signum() >= 0; }
    public boolean isNegative() { return value.signum() < 0; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Margin margin = (Margin) o;
        return Objects.equals(value, margin.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value == null ? "null" : value.toPlainString();
    }
} 