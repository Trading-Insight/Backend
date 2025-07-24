package com.tradin.core.balance.domain.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {
    private final BigDecimal value;

    private Money(BigDecimal value) {
        this.value = applyPolicy(value);
    }

    public static Money of(BigDecimal value) {
        return new Money(value);
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

    public Money add(Money other) {
        BigDecimal result = this.value.add(other.value);
        return Money.of(applyPolicy(result));
    }

    public Money subtract(Money other) {
        BigDecimal result = this.value.subtract(other.value);
        return Money.of(applyPolicy(result));
    }

    public boolean isPositive() { return value.signum() >= 0; }
    public boolean isNegative() { return value.signum() < 0; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(value, money.value);
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