package io.devel.config;

import io.devel.config.loaders.FileConfigurationLoader;
import io.devel.config.loaders.HttpConfigurationLoader;
import io.devel.config.loaders.ResourceConfigurationLoader;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationSourceTest {

    @Test
    void getLoader() {
        final ConfigurationSource configurationSource = new ConfigurationSource(Arrays.asList(
                new FileConfigurationLoader(),
                new HttpConfigurationLoader(),
                new ResourceConfigurationLoader()
        ));

        System.setProperty("config.source", "file://source");
        assertThat(configurationSource.getLoader()).isInstanceOf(FileConfigurationLoader.class);

        System.setProperty("config.source", "http://source");
        assertThat(configurationSource.getLoader()).isInstanceOf(HttpConfigurationLoader.class);

        System.setProperty("config.source", "https://source");
        assertThat(configurationSource.getLoader()).isInstanceOf(HttpConfigurationLoader.class);

        System.setProperty("config.source", "source");
        assertThat(configurationSource.getLoader()).isInstanceOf(ResourceConfigurationLoader.class);

        System.setProperty("config.source", "nothing://source");
        assertThat(configurationSource.getLoader()).isInstanceOf(ResourceConfigurationLoader.class);
    }
}