package io.devel.sql.mappers;

import java.util.Map;
import java.util.function.Function;

/**
 * When mapping a field from and object to a corresponding
 * column in an SQL table, we might need to apply some special
 * operations first. Any sort of 'special' operation to map
 * from the field to the column or the other way around must
 * be wrapped in FiledMapper class.
 */
public class FieldMapper {
    private final String fieldName;
    private final Class<?> targetType;
    private final Function<Object, Map<String, Object>> serializer;
    private final Function<Map<String, Object>, Map<String, Map<String, Object>>> normalizer;

    public FieldMapper(final String fieldName, final Class<?> targetType,
                       final Function<Object, Map<String, Object>> serializer,
                       final Function<Map<String, Object>, Map<String, Map<String, Object>>> normalizer) {
        this.fieldName = fieldName;
        this.targetType = targetType;
        this.serializer = serializer;
        this.normalizer = normalizer;
    }

    public Class<?> getTargetType() {
        return targetType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Map<String, Object> objectToMap(final Object object) {
        return serializer.apply(object);
    }

    public Map<String, Map<String, Object>> normalize(final Map<String, Object> map) {
        return normalizer.apply(map);
    }
}
