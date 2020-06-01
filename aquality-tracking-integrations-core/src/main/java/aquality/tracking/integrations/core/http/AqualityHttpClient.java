package aquality.tracking.integrations.core.http;

import aquality.tracking.integrations.core.AqualityUncheckedException;
import aquality.tracking.integrations.core.configuration.IConfiguration;
import aquality.tracking.integrations.core.utilities.JsonMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import javax.inject.Inject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.apache.http.entity.ContentType.WILDCARD;

public class AqualityHttpClient implements IHttpClient {

    private final IConfiguration configuration;
    private final CloseableHttpClient httpClient;
    private final List<Header> defaultHeaders;

    @Inject
    public AqualityHttpClient(IConfiguration configuration) {
        this.configuration = configuration;
        httpClient = HttpClients.createDefault();
        defaultHeaders = getDefaultHeaders();
    }

    private List<Header> getDefaultHeaders() {
        List<Header> headers = new ArrayList<>();
        headers.add(getBasicAuthHeader());
        headers.add(new BasicHeader(HttpHeaders.ACCEPT, APPLICATION_JSON.getMimeType()));
        headers.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, WILDCARD.getMimeType()));
        return headers;
    }

    private Header getBasicAuthHeader() {
        final String auth = format("project:%d:%s", configuration.getProjectId(), configuration.getToken());
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes());
        String authHeader = "Basic ".concat(new String(encodedAuth));
        return new BasicHeader(HttpHeaders.AUTHORIZATION, authHeader);
    }

    @Override
    public <T> T sendGET(final URI uri, Class<T> tClass) {
        HttpGet request = new HttpGet(uri);
        String responseContent = sendRequest(request);
        return JsonMapper.mapStringContent(responseContent, tClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T sendPOST(final URI uri, final T body) {
        try {
            HttpEntity jsonBody = new StringEntity(JsonMapper.getJson(body));
            String responseContent = sendPOST(uri, jsonBody);
            return (T) JsonMapper.mapStringContent(responseContent, body.getClass());
        } catch (UnsupportedEncodingException e) {
            throw new AqualityUncheckedException(format("Exception occurred during set request body:%n%s", body), e);
        }
    }

    @Override
    public String sendPOST(final URI uri, final HttpEntity entity) {
        HttpPost request = new HttpPost(uri);
        request.setEntity(entity);
        return sendRequest(request);
    }

    private String sendRequest(final HttpRequestBase httpRequest) {
        addHeaders(httpRequest);

        final String response;
        try (CloseableHttpResponse httpResponse = httpClient.execute(httpRequest)) {
            response = processEntity(httpResponse.getEntity());

            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                throw new AqualityUncheckedException(format("Status code: %d. Reason: %s.%nRequest URI: %s",
                        statusCode, response, httpRequest.getURI()));
            }
        } catch (IOException e) {
            throw new AqualityUncheckedException(format("Exception occurred during sending request to %s",
                    httpRequest.getURI()), e);
        } finally {
            httpRequest.releaseConnection();
        }
        return response;
    }

    private void addHeaders(final HttpMessage httpRequest) {
        defaultHeaders.forEach(httpRequest::addHeader);
    }

    private String processEntity(HttpEntity httpEntity) {
        try {
            return EntityUtils.toString(httpEntity, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            throw new AqualityUncheckedException("Exception occurred during processing response entity", e);
        }
    }
}
