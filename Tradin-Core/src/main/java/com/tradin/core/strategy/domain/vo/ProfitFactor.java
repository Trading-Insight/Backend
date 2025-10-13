package com.tradin.core.strategy.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class ProfitFactor {
    private final BigDecimal value;

    private ProfitFactor(BigDecimal value) {
        this.value = applyPolicy(value);
    }

    public static ProfitFactor of(BigDecimal value) {
        return new ProfitFactor(value);
    }

    public BigDecimal getValue() {
        return value;
    }

    private static BigDecimal applyPolicy(BigDecimal value) {
        if (value == null) return null;
        return value.setScale(2, RoundingMode.DOWN);
    }

    @JsonIgnore
    public boolean isPositive() { return value != null && value.signum() >= 0; }

    @JsonIgnore
    public boolean isNegative() { return value != null && value.signum() < 0; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfitFactor that = (ProfitFactor) o;
        return Objects.equals(value, that.value);
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