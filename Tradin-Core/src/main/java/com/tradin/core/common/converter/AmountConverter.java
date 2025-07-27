package com.tradin.core.common.converter;

import com.tradin.core.balance.domain.vo.Amount;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.math.BigDecimal;

@Converter(autoApply = true)
public class AmountConverter implements AttributeConverter<Amount, BigDecimal> {
    @Override
    public BigDecimal convertToDatabaseColumn(Amount attribute) {
        return attribute == null ? null : attribute.getValue();
    }
    @Override
    public Amount convertToEntityAttribute(BigDecimal dbData) {
        return dbData == null ? null : Amount.of(dbData);
    }
} 