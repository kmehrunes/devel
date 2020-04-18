package io.devel.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.devel.jackson.ObjectMappers;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

class ConfigurationFileLoader implements ConfigurationLoader {
    private final List<String> expectedFileNames = Arrays.asList(
            "application.json",
            "application.yaml",
            "application.yml"
    );

    public ConfigContext load() {
        final File configFile = findFirstConfiguration();

        return new JacksonConfigContext(configFile, getMapper(configFile));
    }

    private File findFirstConfiguration() {
        final ClassLoader classLoader = this.getClass().getClassLoader();

        final URL firstConfig = expectedFileNames.stream()
                .map(classLoader::getResource)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Couldn't find a configuration file. Expecting one of: "
                        + expectedFileNames + " to exist"));

        return new File(firstConfig.getFile());
    }

    private ObjectMapper getMapper(final File configFile) {
        if (configFile.getName().endsWith(".yml") || configFile.getName().endsWith(".yaml")) {
            return ObjectMappers.yamlMapper();
        } else {
            return ObjectMappers.jsonMapper();
        }
    }
}
