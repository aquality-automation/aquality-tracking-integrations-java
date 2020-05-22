package aquality.tracking.integrations.core;

import org.apache.http.Header;

import java.net.URI;
import java.util.List;

public interface IHttpClient {

    String sendGET(final URI uri, final List<Header> headers);

    String sendPOST(final URI uri, final List<Header> headers, final String body);
}
