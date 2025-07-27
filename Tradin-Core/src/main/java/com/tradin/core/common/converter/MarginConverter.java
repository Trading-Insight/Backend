package com.tradin.core.common.converter;

import com.tradin.core.futuresOrder.domain.vo.Margin;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.math.BigDecimal;

@Converter(autoApply = true)
public class MarginConverter implements AttributeConverter<Margin, BigDecimal> {
    @Override
    public BigDecimal convertToDatabaseColumn(Margin attribute) {
        return attribute == null ? null : attribute.getValue();
    }
    @Override
    public Margin convertToEntityAttribute(BigDecimal dbData) {
        return dbData == null ? null : Margin.of(dbData);
    }
} 