package io.devel.reflect;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ClassFinderTest {

    @Retention(RetentionPolicy.RUNTIME)
    @interface RuntimeAnnotation { }

    @Retention(RetentionPolicy.SOURCE)
    @interface SourceAnnotation { }

    interface Interface {}

    interface NonImplementedInterface {}

    interface ExtendingInterface extends Interface {}

    static abstract class AbstractImplementation implements ExtendingInterface {}

    static class Implementation extends AbstractImplementation {}

    @RuntimeAnnotation
    static class ImplementationWithAnnotation extends AbstractImplementation {}

    @Test
    void findByImplementAndExtend() {
        final ClassSearch classSearch = new ClassSearch(Collections.singletonList(this.getClass().getPackage().getName()));
        final Set<Class<?>> found = ClassFinder.using(classSearch)
                .implementsInterface(Interface.class)
                .extendsClass(AbstractImplementation.class)
                .find();

        assertThat(found)
                .isEqualTo(new HashSet<>(Arrays.asList(Implementation.class, ImplementationWithAnnotation.class)));
    }

    @Test
    void findByImplementAndExtendAndAnnotation() {
        final ClassSearch classSearch = new ClassSearch(Collections.singletonList(this.getClass().getPackage().getName()));
        final Set<Class<?>> found = ClassFinder.using(classSearch)
                .implementsInterface(Interface.class)
                .extendsClass(AbstractImplementation.class)
                .annotatedWith(RuntimeAnnotation.class)
                .find();

        assertThat(found).isEqualTo(new HashSet<>(Collections.singletonList(ImplementationWithAnnotation.class)));
    }

    @Test
    void findByNonRuntimeAnnotation() {
        final ClassSearch classSearch = new ClassSearch(Collections.singletonList(this.getClass().getPackage().getName()));
        assertThatThrownBy(() -> ClassFinder.using(classSearch).annotatedWith(SourceAnnotation.class))
                .isInstanceOf(IllegalArgumentException.class);
    }
}