package io.devel.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.util.Optional;

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

    public static Optional<ObjectMapper> getForFile(final String fileName) {
        if (fileName.endsWith(".yml") || fileName.endsWith(".yaml")) {
            return Optional.of(yamlMapper());
        } else if (fileName.endsWith(".json")) {
            return Optional.of(jsonMapper());
        }

        return Optional.empty();
    }
}
