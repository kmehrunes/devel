package io.devel.config.loaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.devel.config.ConfigContext;
import io.devel.config.JacksonConfigContext;
import io.devel.jackson.ObjectMappers;

import java.io.File;

/**
 * A configuration loader which reads configuration from a file. It
 * expects either a full path or a relative path from the working
 * directory. It will only resolve configuration sources starting
 * with 'file://'. A full path on a POSIX system will still start
 * with a '/' so it will be 'file:///path/to/configuration/config.json'
 * for example.
 */
public class FileConfigurationLoader implements ConfigurationLoader {
    private static final String PREFIX = "file://";

    @Override
    public ConfigContext load(final String source) {
        if (!canLoad(source)) {
            throw new IllegalArgumentException("Can't load configuration from source " + source);
        }

        final String sanitized = sanitize(source);
        final File file = new File(sanitized);

        return new JacksonConfigContext(file, getMapper(file.getName()));
    }

    @Override
    public boolean canLoad(final String source) {
        return source.startsWith(PREFIX);
    }

    private ObjectMapper getMapper(final String fileName) {
        return ObjectMappers.getForFile(fileName)
                .orElseThrow(() -> new IllegalStateException("Can't read configuration file " + fileName));
    }

    private String sanitize(final String source) {
        return source.substring(PREFIX.length());
    }
}
