package aquality.tracking.integrations.core.endpoints;

import aquality.tracking.integrations.core.AqualityHttpClient;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public abstract class AqualityTrackingEndpoints {

    private final String token = ""; // TODO: get from configuration

    private final AqualityHttpClient httpClient;

    protected AqualityTrackingEndpoints() {
        httpClient = new AqualityHttpClient();
    }

    protected AqualityHttpClient getHttpClient() {
        return httpClient;
    }

    protected List<Header> getHeaders(final String projectId) {
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader(HttpHeaders.ACCEPT, "application/json"));
        headers.add(getBasicAuthHeader(projectId));
        return headers;
    }

    private Header getBasicAuthHeader(final String projectId) {
        byte[] encodedAuth = Base64.encodeBase64(format("project:%s:%s", projectId, token).getBytes());
        String authHeader = "Basic ".concat(new String(encodedAuth));
        return new BasicHeader(HttpHeaders.AUTHORIZATION, authHeader);
    }
}
