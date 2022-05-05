package com.mania.zerosheet.ItemInstances;

import java.util.stream.Stream;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class StatusConverter implements
 AttributeConverter<Status, String> {

    @Override
    public String convertToDatabaseColumn(Status category) {
        if (category == null) {
            return null;
        }
        return category.getCode();
    }

    @Override
    public Status convertToEntityAttribute(String code) {
        if (code == null){
            return null;
        }
        return Stream.of(Status.values())
            .filter(c -> c.getCode().equals(code))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
    
}
