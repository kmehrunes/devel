package io.devel.inject.binders;

import com.google.inject.AbstractModule;
import io.devel.config.ConfigContext;
import io.devel.inject.annotations.Annotated;

public class ConfigurationBinder extends AbstractModule {
    private final ConfigContext configContext;

    public ConfigurationBinder(final ConfigContext configContext) {
        this.configContext = configContext;
    }

    @Override
    protected void configure() {
        configContext.subContexts().forEach(subContext -> {
            bind(ConfigContext.class)
                    .annotatedWith(Annotated.withProperty(subContext))
                    .toInstance(configContext.getSubContext(subContext));
        });

        bind(ConfigContext.class)
                .toInstance(configContext);
    }
}
