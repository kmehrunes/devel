package io.devel.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class JacksonConfigContext implements ConfigContext {
    private final ObjectNode rootNode;
    private final ObjectMapper objectMapper;

    public JacksonConfigContext(final File configFile) {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        this.rootNode = readRootNode(configFile);
    }

    public JacksonConfigContext(final File configFile, final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.rootNode = readRootNode(configFile);
    }

    public JacksonConfigContext(final JsonNode rootNode) {
        if (!rootNode.isObject()) {
            throw new IllegalArgumentException("Invalid JSON configuration");
        }
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        this.rootNode = (ObjectNode) rootNode;
    }

    private ObjectNode readRootNode(final File configFile) {
        try {
            return Optional.ofNullable(objectMapper.readTree(configFile))
                    .filter(JsonNode::isObject)
                    .map(node -> (ObjectNode) node)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid configuration"));
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Object get(final String key) {
        return rootNode.get(key);
    }

    public <T> T getAs(final String key, final Class<T> type) {
        try {
            return objectMapper.treeToValue(rootNode.get(key), type);
        } catch (final JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public <T> Collection<T> getAsCollection(final String key, final Class<T> targetClass) {
        final JsonNode child = rootNode.get(key);

        if (child.isArray()) {
            return objectMapper.convertValue(child,
                    objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, targetClass));
        } else {
            throw new RuntimeException("Field " + key + " does not represent a JSON array");
        }
    }

    public ConfigContext getSubContext(final String key) {
        return new JacksonConfigContext(rootNode.get(key));
    }

    public <T> T convert(final Class<T> clazz) {
        try {
            return objectMapper.treeToValue(rootNode, clazz);
        } catch (final JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Iterable<String> subContexts() {
        return rootNode::fieldNames;
    }

    public String toString() {
        return rootNode.toString();
    }
}
