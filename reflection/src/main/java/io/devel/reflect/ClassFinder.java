package io.devel.reflect;

import io.devel.reflect.exceptions.ReflectException;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassFinder {
    private final ClassSearch classSearch;

    private final Set<Class<?>> superClasses = new HashSet<>();
    private final Set<Class<?>> implementedInterfaces = new HashSet<>();
    private final Set<Class<? extends Annotation>> annotationClasses = new HashSet<>();
    private final Set<Annotation> annotations = new HashSet<>();

    private ClassFinder(final ClassSearch classSearch) {
        this.classSearch = classSearch;
    }

    public static ClassFinder using(final ClassSearch classSearch) {
        return new ClassFinder(classSearch);
    }

    public ClassFinder extendsClass(final Class<?> superClass) {
        if (superClasses.size() > 0) {
            throw new ReflectException("A class can't have more than one superclass");
        }

        superClasses.add(superClass);

        return this;
    }

    public ClassFinder implementsInterface(final Class<?> implementedInterface) {
        if (!implementedInterface.isInterface()) {
            throw new ReflectException(implementedInterface.getName() + " isn't an interface");
        }

        implementedInterfaces.add(implementedInterface);

        return this;
    }

    public ClassFinder annotatedWith(final Class<? extends Annotation> annotation) {
        if (!annotation.isAnnotation()) {
            throw new IllegalArgumentException("Class " + annotation.getSimpleName() + " is not an annotation");
        }

        final Retention retention = annotation.getAnnotation(Retention.class);

        if (retention.value() != RetentionPolicy.RUNTIME) {
            throw new IllegalArgumentException("Annotation " + annotation.getSimpleName() + " has retention "
                    + retention.value() +". Only RUNTIME is allowed");
        }

        annotationClasses.add(annotation);

        return this;
    }

    public ClassFinder annotatedWith(final Annotation annotation) {
        annotations.add(annotation);

        return this;
    }

    public Set<Class<?>> find() {
        final Set<Class<?>> all = combineSuperAndInterfaces();
        Set<Class<?>> found = new HashSet<>();

        for (final Class<?> superclass : all) {
            found = findOrFilterBySuperclass(superclass, found);
        }

        for (final Class<? extends Annotation> annotation : annotationClasses) {
            found = findOrFilterByAnnotation(annotation, found);
        }

        for (final Annotation annotation : annotations) {
            found = findOrFilterByAnnotationValue(annotation, found);
        }

        return found;
    }

    private Set<Class<?>> combineSuperAndInterfaces() {
        return Stream.concat(superClasses.stream(), implementedInterfaces.stream())
                .collect(Collectors.toSet());
    }

    private Set<Class<?>> findOrFilterBySuperclass(final Class<?> superclass, final Set<Class<?>> previous) {
        if (previous.isEmpty()) {
            return classSearch.findAllImplementationClass(superclass)
                    .stream()
                    .map(clazz -> (Class<?>) clazz)
                    .collect(Collectors.toSet());
        } else {
            return previous.stream()
                    .filter(clazz -> ReflectionUtils.hasSuperclass(clazz, superclass))
                    .collect(Collectors.toSet());
        }
    }

    private Set<Class<?>> findOrFilterByAnnotation(final Class<? extends Annotation> annotation, final Set<Class<?>> previous) {
        if (previous.isEmpty()) {
            return classSearch.findClassesByAnnotation(annotation);
        } else {
            return previous.stream()
                    .filter(clazz -> {
                        final Annotation[] annotations = clazz.getAnnotationsByType(annotation);
                        return annotations.length > 0;
                    })
                    .collect(Collectors.toSet());
        }
    }

    private Set<Class<?>> findOrFilterByAnnotationValue(final Annotation annotation, final Set<Class<?>> previous) {
        if (previous.isEmpty()) {
            return classSearch.findClassesByAnnotation(annotation.getClass())
                    .stream()
                    .filter(clazz -> Objects.equals(clazz.getAnnotation(annotation.getClass()), annotation))
                    .collect(Collectors.toSet());
        } else {
            return previous.stream()
                    .filter(clazz -> Objects.equals(clazz.getAnnotation(annotation.getClass()), annotation))
                    .collect(Collectors.toSet());
        }
    }
}
