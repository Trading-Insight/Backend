package com.tradin.core.common.converter;

import com.tradin.core.strategy.domain.vo.ProfitRate;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.math.BigDecimal;

@Converter(autoApply = true)
public class ProfitRateConverter implements AttributeConverter<ProfitRate, BigDecimal> {
    @Override
    public BigDecimal convertToDatabaseColumn(ProfitRate attribute) {
        return attribute == null ? null : attribute.getValue();
    }
    @Override
    public ProfitRate convertToEntityAttribute(BigDecimal dbData) {
        return dbData == null ? null : ProfitRate.of(dbData);
    }
} 