package aquality.tracking.integrations.core.http;

import org.apache.http.HttpEntity;

import java.net.URI;

public interface IHttpClient {

    /**
     * Sends GET request to specified uri.
     * @param uri    Request URI.
     * @param tClass Class of the response data.
     * @param <T>    Type of the response data.
     * @return Response object of type T.
     */
    <T> T sendGET(final URI uri, final Class<T> tClass);

    /**
     * Sends POST request to specified uri.
     * @param uri  Request URI.
     * @param body Request body which converts to string.
     * @param <T>  Type of the response/request body data.
     * @return Response object of type T.
     */
    <T> T sendPOST(final URI uri, final T body);

    /**
     * Sends POST request to specified uri.
     * @param uri    Request URI.
     * @param entity Request entity.
     * @return Response as string.
     */
    String sendPOST(final URI uri, final HttpEntity entity);
}
