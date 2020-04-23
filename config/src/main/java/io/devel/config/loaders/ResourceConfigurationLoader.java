package io.devel.config.loaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.devel.config.ConfigContext;
import io.devel.config.JacksonConfigContext;
import io.devel.jackson.ObjectMappers;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A configuration loader which loads from resources. It accepts an
 * optional configuration source. If no source was specified then it
 * will look for one of the default configuration file names. It uses
 * a ClassLoader to find the resource.
 */
public class ResourceConfigurationLoader implements ConfigurationLoader {
    private final List<String> defaultFileNames = Arrays.asList(
            "application.json",
            "application.yaml",
            "application.yml"
    );

    @Override
    public ConfigContext load(final String source) {
        if (source == null) {
            return findDefaultConfiguration();
        }

        if (!canLoad(source)) {
            throw new IllegalArgumentException("Can't load configuration from source " + source);
        }

        return Optional.of(this.getClass().getClassLoader())
                .map(classLoader -> classLoader.getResource(source))
                .map(URL::getFile)
                .map(path -> new JacksonConfigContext(new File(path), getMapper(path)))
                .orElseThrow(() -> new IllegalStateException("Couldn't find a configuration file " + source + " in resources"));
    }

    /**
     * Resources don't need a protocol before them. This will
     * match anything which doesn't start with '[any letters]://'.
     */
    @Override
    public boolean canLoad(final String source) {
        return !source.matches("^\\w+://.*");
    }

    private ConfigContext findDefaultConfiguration() {
        final File configFile = findFirstConfiguration();

        return new JacksonConfigContext(configFile, getMapper(configFile));
    }

    private File findFirstConfiguration() {
        final ClassLoader classLoader = this.getClass().getClassLoader();

        final URL firstConfig = defaultFileNames.stream()
                .map(classLoader::getResource)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Couldn't find a configuration file. Expecting one of: "
                        + defaultFileNames + " to exist"));

        return new File(firstConfig.getFile());
    }

    private ObjectMapper getMapper(final File configFile) {
        return getMapper(configFile.getName());
    }

    private ObjectMapper getMapper(final String fileName) {
        return ObjectMappers.getForFile(fileName)
                .orElseThrow(() -> new IllegalStateException("Can't read configuration file " + fileName));
    }
}
