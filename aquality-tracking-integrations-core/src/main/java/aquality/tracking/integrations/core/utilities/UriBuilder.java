package aquality.tracking.integrations.core.utilities;

import aquality.tracking.integrations.core.AqualityUncheckedException;
import org.apache.http.client.utils.URIBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

public class UriBuilder {

    private final URIBuilder builder;
    private final Map<String, String> queryParameters;

    public UriBuilder(String host) {
        builder = new URIBuilder();
        builder.setHost(host);
        queryParameters = new HashMap<>();
    }

    public UriBuilder setPath(final String path) {
        builder.setPath(path);
        return this;
    }

    public <T> UriBuilder setParameter(final String name, final T value) {
        queryParameters.put(name, value.toString());
        return this;
    }

    public URI build() {
        try {
            queryParameters.forEach((name, value) -> builder.setParameter(name, encodeParameter(value)));
            return builder.build();
        } catch (URISyntaxException e) {
            throw new AqualityUncheckedException("Exception during building URI", e);
        }
    }

    private String encodeParameter(final String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new AqualityUncheckedException(format("Exception occurred while encoding query parameter: %s", value), e);
        }
    }
}
