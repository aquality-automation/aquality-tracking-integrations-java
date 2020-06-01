package aquality.tracking.integrations.core.endpoints.impl;

import aquality.tracking.integrations.core.AqualityUncheckedException;
import aquality.tracking.integrations.core.configuration.IConfiguration;
import aquality.tracking.integrations.core.http.IHttpClient;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicHeader;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

public abstract class AqualityTrackingEndpoints {

    @Getter(AccessLevel.PROTECTED)
    private final IConfiguration configuration;
    @Getter(AccessLevel.PROTECTED)
    private final IHttpClient httpClient;

    protected AqualityTrackingEndpoints(IConfiguration configuration, IHttpClient httpClient) {
        this.configuration = configuration;
        this.httpClient = httpClient;
    }



    protected URI buildURI(final String path) {
        return buildURI(path, new HashMap<>());
    }

    protected URI buildURI(final String path, final Map<String, String> queryParams) {
        try {
            URIBuilder uriBuilder = new URIBuilder(configuration.getHost());
            uriBuilder.setPath(path);
            queryParams.forEach((param, value) -> uriBuilder.setParameter(param, encodeParameter(value)));
            return uriBuilder.build();
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
