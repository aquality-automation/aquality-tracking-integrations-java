package aquality.tracking.integrations.core.endpoints.impl;

import aquality.tracking.integrations.core.AqualityUncheckedException;
import aquality.tracking.integrations.core.Configuration;
import aquality.tracking.integrations.core.IHttpClient;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public abstract class AqualityTrackingEndpoints {

    @Getter(AccessLevel.PROTECTED)
    private final Configuration configuration;
    @Getter(AccessLevel.PROTECTED)
    private final IHttpClient httpClient;

    protected AqualityTrackingEndpoints(Configuration configuration, IHttpClient httpClient) {
        this.configuration = configuration;
        this.httpClient = httpClient;
    }

    protected List<Header> getHeaders() {
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader(HttpHeaders.ACCEPT, "application/json"));
        headers.add(getBasicAuthHeader());
        return headers;
    }

    private Header getBasicAuthHeader() {
        final String auth = format("project:%d:%s", configuration.getProjectId(), configuration.getToken());
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes());
        String authHeader = "Basic ".concat(new String(encodedAuth));
        return new BasicHeader(HttpHeaders.AUTHORIZATION, authHeader);
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
            throw new AqualityUncheckedException(format("Exception occurred while encoding query parameter : %s", value), e);
        }
    }
}
