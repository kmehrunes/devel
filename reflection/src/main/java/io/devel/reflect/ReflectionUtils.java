package io.devel.reflect;

public class ReflectionUtils {
    public static boolean hasSuperclass(final Class<?> subclass, final Class<?> targetSuperclass) {
        if (subclass == Object.class) {
            return false;
        }

        if (subclass.getSuperclass() == targetSuperclass) {
            return true;
        }

        return hasSuperclass(subclass.getSuperclass(), targetSuperclass);
    }
}
