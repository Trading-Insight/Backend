package com.tradin.core.balance.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Amount {
    private final BigDecimal value;

    private Amount(BigDecimal value) {
        this.value = applyPolicy(value);
    }

    public static Amount of(BigDecimal value) {
        return new Amount(value);
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

    public Amount add(Amount other) {
        BigDecimal result = this.value.add(other.value);
        return Amount.of(applyPolicy(result));
    }

    public Amount subtract(Amount other) {
        BigDecimal result = this.value.subtract(other.value);
        return Amount.of(applyPolicy(result));
    }

    public Amount negate() {
        BigDecimal result = this.value.negate();
        return Amount.of(applyPolicy(result));
    }

    @JsonIgnore
    public boolean isPositive() { return value.signum() >= 0; }

    @JsonIgnore
    public boolean isNegative() { return value.signum() < 0; }
    public boolean isLessThan(Amount other) {
        return this.value.compareTo(other.value) < 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amount amount = (Amount) o;
        return Objects.equals(value, amount.value);
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