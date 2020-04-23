package io.devel.config;

import io.devel.config.loaders.ConfigurationLoader;
import io.devel.config.loaders.ResourceConfigurationLoader;

import java.util.List;
import java.util.Optional;

public class ConfigurationSource {
    private final static String CONFIG_SOURCE_PROPERTY = "config.source";

    private final ConfigurationLoader fallback = new ResourceConfigurationLoader();

    private final List<ConfigurationLoader> configurationLoaders;

    public ConfigurationSource(final List<ConfigurationLoader> configurationLoaders) {
        this.configurationLoaders = configurationLoaders;
    }

    public ConfigurationLoader getLoader() {
        return getSourceFromSystem()
                .flatMap(this::getLoaderFromSource)
                .orElse(fallback);
    }

    private Optional<String> getSourceFromSystem() {
        return Optional.ofNullable(System.getProperty(CONFIG_SOURCE_PROPERTY));
    }

    private Optional<ConfigurationLoader> getLoaderFromSource(final String source) {
        return configurationLoaders.stream()
                .filter(loader -> loader.canLoad(source))
                .findFirst();
    }
}
