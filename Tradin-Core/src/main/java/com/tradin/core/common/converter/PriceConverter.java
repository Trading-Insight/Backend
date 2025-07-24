package com.tradin.core.common.converter;

import com.tradin.core.price.domain.vo.Price;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.math.BigDecimal;

@Converter(autoApply = true)
public class PriceConverter implements AttributeConverter<Price, BigDecimal> {
    @Override
    public BigDecimal convertToDatabaseColumn(Price attribute) {
        return attribute == null ? null : attribute.getValue();
    }
    @Override
    public Price convertToEntityAttribute(BigDecimal dbData) {
        return dbData == null ? null : Price.of(dbData);
    }
} 