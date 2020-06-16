package io.devel.sql.mappers;

public class FieldMappers {
    public static FieldMapper shallow(final Class<?> clazz, final String fieldName) {
        try {
            return ShallowMappersFactory.create(clazz.getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
