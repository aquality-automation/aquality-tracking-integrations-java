package aquality.tracking.integrations.core;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.lang.String.format;

public class AqualityHttpClient implements IHttpClient {

    @Override
    public String sendGET(final URI uri, final List<Header> headers) {
        HttpGet request = new HttpGet(uri);
        addHeaders(request, headers);
        return sendRequest(request);
    }

    @Override
    public String sendPOST(final URI uri, final List<Header> headers, final String body) {
        try {
            return sendPOST(uri, headers, new StringEntity(body));
        } catch (UnsupportedEncodingException e) {
            throw new AqualityUncheckedException(format("Exception occurred during set request body:%n%s", body), e);
        }
    }

    @Override
    public String sendPOST(final URI uri, final List<Header> headers, final HttpEntity entity) {
        HttpPost request = new HttpPost(uri);
        addHeaders(request, headers);
        request.setEntity(entity);
        return sendRequest(request);
    }

    private String sendRequest(final HttpUriRequest httpRequest) {
        final String response;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            try (CloseableHttpResponse httpResponse = httpClient.execute(httpRequest)) {
                response = processEntity(httpResponse.getEntity());

                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    throw new AqualityUncheckedException(format("Status code: %d. Reason: %s.%nRequest URI: %s",
                            statusCode, response, httpRequest.getURI()));
                }
            }
        } catch (IOException e) {
            throw new AqualityUncheckedException(format("Exception occurred during sending request to %s",
                    httpRequest.getURI()), e);
        }
        return response;
    }

    private void addHeaders(HttpRequest request, List<Header> headers) {
        headers.forEach(request::addHeader);
    }

    private String processEntity(HttpEntity httpEntity) {
        try {
            return EntityUtils.toString(httpEntity, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            throw new AqualityUncheckedException("Exception occurred during processing response entity", e);
        }
    }
}
