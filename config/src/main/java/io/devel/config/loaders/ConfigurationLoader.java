package io.devel.config.loaders;

import io.devel.config.ConfigContext;

public interface ConfigurationLoader {
    ConfigContext load(String source);
    boolean canLoad(String source);
}
