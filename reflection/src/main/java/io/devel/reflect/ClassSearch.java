package io.devel.reflect;

import io.devel.reflect.exceptions.NoImplementationFoundException;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassSearch {
    private final Reflections reflections;

    public ClassSearch(final Collection<String> searchPackages) {
        this.reflections = new Reflections(searchPackages);
    }

    public ClassSearch(final Reflections reflections) {
        this.reflections = reflections;
    }

    public <T> Class<? extends T> findImplementationClass(final Class<T> base) throws NoImplementationFoundException {
        final Set<Class<? extends T>> implementations = reflections.getSubTypesOf(base);

        return implementations.stream()
                .filter(clazz -> !clazz.isInterface())
                .findFirst()
                .orElseThrow(() -> new NoImplementationFoundException("No class implementation was found for " + base.getSimpleName()));
    }

    public <T> Set<Class<? extends T>> findAllImplementationClass(final Class<T> base) {
        final Set<Class<? extends T>> implementations = reflections.getSubTypesOf(base);

        return implementations.stream()
                .filter(clazz -> !clazz.isInterface())
                .collect(Collectors.toSet());
    }

    public <T> Set<Class<?>> findClassesByAnnotation(final Class<? extends Annotation> annotationClass) {
        return reflections.getTypesAnnotatedWith(annotationClass);
    }
}
