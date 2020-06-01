package aquality.tracking.integrations.core.http;

import org.apache.http.HttpEntity;

import java.net.URI;

public interface IHttpClient {

    /**
     * Sends GET request to specified uri.
     * @param uri Request URI.
     * @return Response as string.
     */
    String sendGET(final URI uri);

    /**
     * Sends POST request to specified uri.
     * @param uri  Request URI.
     * @param body JSON request body as string.
     * @return Response as string.
     */
    String sendPOST(final URI uri, final String body);

    /**
     * Sends POST request to specified uri.
     * @param uri    Request URI.
     * @param entity Request entity.
     * @return Response as string.
     */
    String sendPOST(final URI uri, final HttpEntity entity);
}
