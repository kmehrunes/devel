package io.devel.config.loaders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.devel.config.ConfigContext;
import io.devel.config.JacksonConfigContext;
import io.devel.jackson.ObjectMappers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

/**
 * A configuration loader which gets the configuration from a remote
 * URL. It supports HTTP and HTTPS, and will read JSON or YAML based
 * on the content type in the response header. If not type was set it
 * will default to using JSON.
 *
 * Since the configuration server might require client authorization
 * in the request, it could optionally be set by either the system
 * property config.http.authorization or the environment variable
 * CONFIG_HTTP_AUTHORIZATION.
 */
public class HttpConfigurationLoader implements ConfigurationLoader {
    private static final String HTTP_PREFIX = "http://";
    private static final String HTTPS_PREFIX = "https://";

    private static final String AUTHORIZATION_SYSTEM_PROPERTY = "config.http.authorization";
    private static final String AUTHORIZATION_ENV_VARIABLE = "CONFIG_HTTP_AUTHORIZATION";

    @Override
    public ConfigContext load(final String source) {
        if (!canLoad(source)) {
            throw new IllegalArgumentException("Can't load configuration from source " + source);
        }

        try {
            final HttpResponse<String> response = getFromUrl(source);
            return parseConfig(response);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load configuration from " + source);
        }
    }

    @Override
    public boolean canLoad(final String source) {
        return source.startsWith(HTTP_PREFIX) || source.startsWith(HTTPS_PREFIX);
    }

    private HttpResponse<String> getFromUrl(final String url) throws IOException, InterruptedException {
        final HttpRequest httpRequest = buildRequest(url);

        final HttpClient httpClient = HttpClient.newHttpClient();

        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpRequest buildRequest(final String url) {
        final URI uri = URI.create(url);
        final String authorization = readAuthorization();

        final HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .GET()
                .uri(uri);

        if (authorization != null) {
            requestBuilder.header("Authorization", authorization);
        }

        return requestBuilder.build();
    }

    private String readAuthorization() {
        return Optional.ofNullable(System.getProperty(AUTHORIZATION_SYSTEM_PROPERTY))
                .orElseGet(() -> System.getenv(AUTHORIZATION_ENV_VARIABLE));
    }

    private ConfigContext parseConfig(final HttpResponse<String> response) throws JsonProcessingException {
        final ObjectMapper objectMapper = getMapper(response.headers());
        final JsonNode rootNode = objectMapper.readTree(response.body());

        return new JacksonConfigContext(rootNode);
    }

    private ObjectMapper getMapper(final HttpHeaders headers) {
        final Optional<String> contentType = headers.firstValue("Content-Type");

        return contentType
                .map(this::getMapperByContentType)
                .orElseGet(ObjectMappers::jsonMapper);
    }

    private ObjectMapper getMapperByContentType(final String contentType) {
        if (contentType.startsWith("application/json")) {
            return ObjectMappers.jsonMapper();
        } else if (contentType.startsWith("application/yaml")) {
            return ObjectMappers.jsonMapper();
        } else {
            throw new IllegalArgumentException("Unrecognized content type " + contentType);
        }
    }
}
