package io.devel.inject.annotations;

import java.lang.annotation.Annotation;

public class WithProperty implements Property {
    private final String property;

    public WithProperty(final String property) {
        this.property = property;
    }

    @Override
    public String value() {
        return property;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Property.class;
    }
}
