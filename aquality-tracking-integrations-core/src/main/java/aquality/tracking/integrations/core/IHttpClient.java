package aquality.tracking.integrations.core;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import java.net.URI;
import java.util.List;

public interface IHttpClient {

    /**
     * Sends GET request to specified uri.
     * @param uri     Request URI.
     * @param headers Request headers.
     * @return Response as string.
     */
    String sendGET(final URI uri, final List<Header> headers);

    /**
     * Sends POST request to specified uri.
     * @param uri     Request URI.
     * @param headers Request headers.
     * @param body    JSON request body as string.
     * @return Response as string.
     */
    String sendPOST(final URI uri, final List<Header> headers, final String body);

    /**
     * Sends POST request to specified uri.
     * @param uri     Request URI.
     * @param headers Request headers.
     * @param entity  Request entity.
     * @return Response as string.
     */
    String sendPOST(final URI uri, final List<Header> headers, final HttpEntity entity);
}
