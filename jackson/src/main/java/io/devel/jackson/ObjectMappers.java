package io.devel.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class ObjectMappers {
    public static ObjectMapper yamlMapper() {
        return configureMapper(new ObjectMapper(new YAMLFactory()));
    }

    public static ObjectMapper jsonMapper() {
        return configureMapper(new ObjectMapper());
    }

    public static ObjectMapper configureMapper(final ObjectMapper objectMapper) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}
