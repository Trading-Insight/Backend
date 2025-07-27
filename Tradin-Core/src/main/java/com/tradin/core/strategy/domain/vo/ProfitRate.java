package com.tradin.core.strategy.domain.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class ProfitRate {
    private final BigDecimal value;

    private ProfitRate(BigDecimal value) {
        this.value = applyPolicy(value);
    }

    public static ProfitRate of(BigDecimal value) {
        return new ProfitRate(value);
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

    public ProfitRate add(ProfitRate other) {
        BigDecimal result = this.value.add(other.value);
        return ProfitRate.of(applyPolicy(result));
    }

    public ProfitRate subtract(ProfitRate other) {
        BigDecimal result = this.value.subtract(other.value);
        return ProfitRate.of(applyPolicy(result));
    }

    public boolean isPositive() { return value.signum() >= 0; }
    public boolean isNegative() { return value.signum() < 0; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfitRate that = (ProfitRate) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toPlainString();
    }
} 