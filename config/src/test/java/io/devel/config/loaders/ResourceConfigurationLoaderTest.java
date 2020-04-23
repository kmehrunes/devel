package io.devel.config.loaders;

import io.devel.config.ConfigContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResourceConfigurationLoaderTest {

    @Test
    void canLoadValidResource() {
        final ResourceConfigurationLoader loader = new ResourceConfigurationLoader();
        final String validResource = "application.yaml";

        assertThat(loader.canLoad(validResource)).isTrue();
    }

    @Test
    void canLoadInvalidResource() {
        final ResourceConfigurationLoader loader = new ResourceConfigurationLoader();
        final String invalidResource = "file://application.yaml";

        assertThat(loader.canLoad(invalidResource)).isFalse();
    }

    @Test
    void loadInvalidResource() {
        final ResourceConfigurationLoader loader = new ResourceConfigurationLoader();
        final String invalidResource = "file://application.yaml";

        assertThatThrownBy(() -> loader.load(invalidResource)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void loadJsonResource() {
        final ResourceConfigurationLoader loader = new ResourceConfigurationLoader();
        final ConfigContext configContext = loader.load("config.json");

        assertThat(configContext).isNotNull();
        assertThat(configContext.getAs("config", Boolean.class)).isTrue();
    }

    @Test
    void loadYamlResource() {
        final ResourceConfigurationLoader loader = new ResourceConfigurationLoader();
        final ConfigContext configContext = loader.load("config.yaml");

        assertThat(configContext).isNotNull();
        assertThat(configContext.getAs("config", Boolean.class)).isTrue();
    }

    @Test
    void loadResourceNotFound() {
        final ResourceConfigurationLoader loader = new ResourceConfigurationLoader();
        assertThatThrownBy(() -> loader.load("missing.yaml")).isInstanceOf(IllegalStateException.class);
    }
}