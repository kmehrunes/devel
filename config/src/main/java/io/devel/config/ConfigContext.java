package io.devel.config;

import java.util.Collection;

public interface ConfigContext {
    Object get(String key);
    <T> T getAs(String key, Class<T> type);
    <T> Collection<T> getAsCollection(String key, Class<T> targetClass);

    ConfigContext getSubContext(String key);

    <T> T convert(Class<T> clazz);

    Iterable<String> subContexts();
}
