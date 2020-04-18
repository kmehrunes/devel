package io.devel.inject.annotations;

import java.lang.annotation.Annotation;

class WithFormat implements Format {
    private final String format;

    public WithFormat(final String format) {
        this.format = format;
    }

    @Override
    public String value() {
        return format;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Format.class;
    }
}
