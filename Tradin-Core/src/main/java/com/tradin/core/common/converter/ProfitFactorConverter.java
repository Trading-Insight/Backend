package com.tradin.core.common.converter;

import com.tradin.core.strategy.domain.vo.ProfitFactor;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.math.BigDecimal;

@Converter(autoApply = true)
public class ProfitFactorConverter implements AttributeConverter<ProfitFactor, BigDecimal> {
    @Override
    public BigDecimal convertToDatabaseColumn(ProfitFactor attribute) {
        return attribute == null ? null : attribute.getValue();
    }
    @Override
    public ProfitFactor convertToEntityAttribute(BigDecimal dbData) {
        return dbData == null ? null : ProfitFactor.of(dbData);
    }
} 