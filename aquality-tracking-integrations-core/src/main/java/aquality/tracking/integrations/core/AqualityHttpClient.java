package aquality.tracking.integrations.core;

import org.apache.http.*;
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

public class AqualityHttpClient {

    public String sendGET(final URI uri, final List<Header> headers) throws AqualityException {
        HttpGet request = new HttpGet(uri);
        addHeaders(request, headers);
        return sendRequest(request);
    }

    public String sendPOST(final URI uri, final List<Header> headers, final String body) throws AqualityException {
        HttpPost request = new HttpPost(uri);
        addHeaders(request, headers);
        try {
            request.setEntity(new StringEntity(body));
        } catch (UnsupportedEncodingException e) {
            throw new AqualityException("Exception occurred during set request body", e);
        }
        return sendRequest(request);
    }

    private String sendRequest(final HttpUriRequest httpRequest) throws AqualityException {
        final String response;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            try (CloseableHttpResponse httpResponse = httpClient.execute(httpRequest)) {
                processStatusLine(httpResponse.getStatusLine());
                response = processEntity(httpResponse.getEntity());
            }
        } catch (IOException e) {
            throw new AqualityException(format("Exception occurred during sending request to %s",
                    httpRequest.getURI()), e);
        }
        return response;
    }

    private void addHeaders(HttpRequest request, List<Header> headers) {
        headers.forEach(request::addHeader);
    }

    private void processStatusLine(StatusLine statusLine) throws AqualityException {
        int statusCode = statusLine.getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            throw new AqualityException(format("Bad status code: %d. Reason: %s",
                    statusCode, statusLine.getReasonPhrase()));
        }
    }

    private String processEntity(HttpEntity httpEntity) throws IOException {
        return EntityUtils.toString(httpEntity, StandardCharsets.UTF_8.name());
    }
}
