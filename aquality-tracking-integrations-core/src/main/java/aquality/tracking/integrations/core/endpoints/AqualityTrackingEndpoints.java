package aquality.tracking.integrations.core.endpoints;

import aquality.tracking.integrations.core.AqualityHttpClient;
import aquality.tracking.integrations.core.Configuration;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicHeader;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public abstract class AqualityTrackingEndpoints {

    protected static final Configuration CONFIG = Configuration.getInstance();

    private final AqualityHttpClient httpClient;

    protected AqualityTrackingEndpoints() {
        httpClient = new AqualityHttpClient();
    }

    protected AqualityHttpClient getHttpClient() {
        return httpClient;
    }

    protected List<Header> getHeaders() {
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader(HttpHeaders.ACCEPT, "application/json"));
        headers.add(getBasicAuthHeader());
        return headers;
    }

    private Header getBasicAuthHeader() {
        final String auth = format("project:%d:%s", CONFIG.getProjectId(), CONFIG.getToken());
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes());
        String authHeader = "Basic ".concat(new String(encodedAuth));
        return new BasicHeader(HttpHeaders.AUTHORIZATION, authHeader);
    }

    protected URI buildURI(final String path) {
        return buildURI(path, new HashMap<>());
    }

    protected URI buildURI(final String path, final Map<String, String> params) {
        URI uri;
        try {
            URIBuilder uriBuilder = new URIBuilder(CONFIG.getHost());
            uriBuilder.setPath(path);
            params.forEach(uriBuilder::setParameter);
            uri = uriBuilder.build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Exception during build URI", e);
        }
        return uri;
    }
}
