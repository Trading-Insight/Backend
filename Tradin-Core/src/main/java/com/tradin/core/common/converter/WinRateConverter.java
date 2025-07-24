package com.tradin.core.common.converter;

import com.tradin.core.strategy.domain.vo.WinRate;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.math.BigDecimal;

@Converter(autoApply = true)
public class WinRateConverter implements AttributeConverter<WinRate, BigDecimal> {
    @Override
    public BigDecimal convertToDatabaseColumn(WinRate attribute) {
        return attribute == null ? null : attribute.getValue();
    }
    @Override
    public WinRate convertToEntityAttribute(BigDecimal dbData) {
        return dbData == null ? null : WinRate.of(dbData);
    }
} 