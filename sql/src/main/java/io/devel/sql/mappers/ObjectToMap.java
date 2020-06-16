package io.devel.sql.mappers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ObjectToMap {
    private final Map<String, FieldMapper> mappers;

    public ObjectToMap() {
        this.mappers = Collections.emptyMap();
    }

    public ObjectToMap(final List<FieldMapper> mappers) {
        this.mappers = mappers.stream()
                .collect(Collectors.toMap(FieldMapper::getFieldName, Function.identity()));
    }

    public <T> Map<String, Object> substitutionMap(final T object) {
        final Map<String, Object> subs = new HashMap<>();

        final List<Field> specialFields = new ArrayList<>();

        Stream.of(object.getClass().getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .filter(field -> {
                    if (mappers.containsKey(field.getName())) {
                        specialFields.add(field);
                        return false;
                    }

                    return true;
                })
                .peek(field -> field.setAccessible(true))
                .forEach(field-> subs.put(field.getName(), getValue(field, object)));

        specialFields.forEach(field -> {
            field.setAccessible(true);
            subs.putAll(mappers.get(field.getName()).objectToMap(getValue(field, object)));
        });

        return subs;
    }

    private <T> Object getValue(final Field field, final T object) {
        try {
            if (field.getType().isAssignableFrom(List.class) || field.getType().isEnum()) {
                return field.get(object).toString();
            } else {
                return field.get(object);
            }
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
