package io.devel.inject.annotations;

public class Annotated {
    private Annotated() {}

    public static Format withFormat(final String format) {
        return new WithFormat(format);
    }

    public static Property withProperty(final String property) {
        return new WithProperty(property);
    }
}
