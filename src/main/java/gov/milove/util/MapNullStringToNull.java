package gov.milove.util;

import com.github.dozermapper.core.CustomConverter;

public class MapNullStringToNull implements CustomConverter {

    @Override
    public Object convert(Object o, Object o1, Class<?> aClass, Class<?> aClass1) {
        if (o1 == null) return null;
        if ((o1).equals("null")) {
            return null;
        }
        return Long.parseLong(o1.toString());
    }
}
