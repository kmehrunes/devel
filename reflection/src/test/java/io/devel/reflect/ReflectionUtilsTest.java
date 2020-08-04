package io.devel.reflect;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionUtilsTest {

    static class SuperClass {}

    static class SubClass extends SuperClass {}

    @Test
    void hasSuperclass() {
        assertThat(ReflectionUtils.hasSuperclass(SubClass.class, SuperClass.class)).isTrue();
    }

    @Test
    void doesNotSuperclass() {
        assertThat(ReflectionUtils.hasSuperclass(SubClass.class, String.class)).isFalse();
    }
}