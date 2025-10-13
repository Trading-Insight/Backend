package com.tradin.core.strategy.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class WinRate {
    private final BigDecimal value;

    private WinRate(BigDecimal value) {
        this.value = applyPolicy(value);
    }

    public static WinRate of(BigDecimal value) {
        return new WinRate(value);
    }

    public BigDecimal getValue() {
        return value;
    }

    private static BigDecimal applyPolicy(BigDecimal value) {
        if (value == null) return null;
        return value.setScale(2, RoundingMode.DOWN);
    }

    public WinRate add(WinRate other) {
        return WinRate.of(this.value.add(other.value));
    }

    public WinRate subtract(WinRate other) {
        return WinRate.of(this.value.subtract(other.value));
    }

    @JsonIgnore
    public boolean isPositive() { return value.signum() >= 0; }

    @JsonIgnore
    public boolean isNegative() { return value.signum() < 0; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WinRate winRate = (WinRate) o;
        return Objects.equals(value, winRate.value);
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