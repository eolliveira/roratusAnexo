package br.ind.ajrorato.gateway.database.converters;

import br.ind.ajrorato.gateway.database.entity.UserRole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StringToBooleanConverter implements AttributeConverter<UserRole, String> {
    @Override
    public String convertToDatabaseColumn(UserRole attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public UserRole convertToEntityAttribute(String dbData) {
        return dbData != null ? UserRole.fromValue(dbData) : null;
    }
}
