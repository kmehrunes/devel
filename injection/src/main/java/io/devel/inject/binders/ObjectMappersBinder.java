package io.devel.inject.binders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import io.devel.inject.annotations.Annotated;
import io.devel.jackson.ObjectMappers;

public class ObjectMappersBinder extends AbstractModule {
    @Override
    protected void configure() {
        bind(ObjectMapper.class)
                .annotatedWith(Annotated.withFormat("yaml"))
                .toInstance(ObjectMappers.yamlMapper());

        bind(ObjectMapper.class)
                .annotatedWith(Annotated.withFormat("json"))
                .toInstance(ObjectMappers.yamlMapper());

        bind(ObjectMapper.class)
                .toInstance(ObjectMappers.yamlMapper());
    }
}
