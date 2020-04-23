package io.devel.config.loaders;

import io.devel.config.ConfigContext;
import org.junit.jupiter.api.Test;

import java.io.UncheckedIOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileConfigurationLoaderTest {
    @Test
    void canLoadValidResource() {
        final FileConfigurationLoader loader = new FileConfigurationLoader();
        final String validResource = "file://application.yaml";

        assertThat(loader.canLoad(validResource)).isTrue();
    }

    @Test
    void canLoadInvalidResource() {
        final FileConfigurationLoader loader = new FileConfigurationLoader();
        final String invalidResource = "application.yaml";

        assertThat(loader.canLoad(invalidResource)).isFalse();
    }

    @Test
    void loadInvalidResource() {
        final FileConfigurationLoader loader = new FileConfigurationLoader();
        final String invalidResource = "http://application.yaml";

        assertThatThrownBy(() -> loader.load(invalidResource)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void loadJsonResource() {
        final FileConfigurationLoader loader = new FileConfigurationLoader();
        final ConfigContext configContext = loader.load("file://target/test-classes/config.json");

        assertThat(configContext).isNotNull();
        assertThat(configContext.getAs("config", Boolean.class)).isTrue();
    }

    @Test
    void loadYamlResource() {
        final FileConfigurationLoader loader = new FileConfigurationLoader();
        final ConfigContext configContext = loader.load("file://target/test-classes/config.yaml");

        assertThat(configContext).isNotNull();
        assertThat(configContext.getAs("config", Boolean.class)).isTrue();
    }

    @Test
    void loadResourceNotFound() {
        final FileConfigurationLoader loader = new FileConfigurationLoader();
        assertThatThrownBy(() -> loader.load("file://missing.yaml")).isInstanceOf(UncheckedIOException.class);
    }
}