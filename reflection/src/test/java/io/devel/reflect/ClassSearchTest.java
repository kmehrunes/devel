package io.devel.reflect;

import io.devel.reflect.exceptions.NoImplementationFoundException;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ClassSearchTest {

    interface Interface {}

    interface NonImplementedInterface {}

    interface ExtendingInterface extends Interface {}

    static abstract class AbstractImplementation implements ExtendingInterface {}

    static class Implementation extends AbstractImplementation {}

    @Test
    void findImplementationClass() throws NoImplementationFoundException {
        final ClassSearch classSearch = new ClassSearch(Collections.singletonList(this.getClass().getPackage().getName()));

        assertThat(classSearch.findImplementationClass(Interface.class)).isEqualTo(Implementation.class);
    }

    @Test
    void findImplementationClassNoImplementation() {
        final ClassSearch classSearch = new ClassSearch(Collections.singletonList(this.getClass().getPackage().getName()));

        assertThatThrownBy(() -> classSearch.findImplementationClass(NonImplementedInterface.class))
                .isInstanceOf(NoImplementationFoundException.class);
    }

    @Test
    void findAllImplementationClass() {
        final ClassSearch classSearch = new ClassSearch(Collections.singletonList(this.getClass().getPackage().getName()));

        assertThat(classSearch.findAllImplementationClass(Interface.class))
                .isEqualTo(new HashSet<>(Collections.singletonList(Implementation.class)));
    }

}