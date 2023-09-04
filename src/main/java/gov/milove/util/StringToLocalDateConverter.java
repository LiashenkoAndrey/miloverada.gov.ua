package gov.milove.util;

import com.github.dozermapper.core.CustomConverter;

import java.time.LocalDate;

public class StringToLocalDateConverter implements CustomConverter {

    @Override
    public Object convert(Object o, Object o1, Class<?> aClass, Class<?> aClass1) {
        if (o1 == null) return null;
        if (!o1.toString().isEmpty()) {
            return LocalDate.parse(o1.toString());
        } else {
            return o;
        }
    }
}