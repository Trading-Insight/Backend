package com.tradin.core.price.domain.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Price {
    private final BigDecimal value;

    private Price(BigDecimal value) {
        this.value = applyPolicy(value);
    }

    public static Price of(BigDecimal value) {
        return new Price(value);
    }

    @JsonCreator
    public static Price from(BigDecimal value) {
        return of(value);
    }

    @JsonCreator
    public static Price from(String value) {
        return of(new BigDecimal(value));
    }

    @JsonCreator
    public static Price from(Number value) {
        return of(new BigDecimal(value.toString()));
    }

    public BigDecimal getValue() {
        return value;
    }

    @JsonValue
    public BigDecimal toJsonValue() {
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

    public Price add(Price other) {
        BigDecimal result = this.value.add(other.value);
        return Price.of(applyPolicy(result));
    }

    public Price subtract(Price other) {
        BigDecimal result = this.value.subtract(other.value);
        return Price.of(applyPolicy(result));
    }

    @JsonIgnore
    public boolean isPositive() { return value.signum() >= 0; }

    @JsonIgnore
    public boolean isNegative() { return value.signum() < 0; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(value, price.value);
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