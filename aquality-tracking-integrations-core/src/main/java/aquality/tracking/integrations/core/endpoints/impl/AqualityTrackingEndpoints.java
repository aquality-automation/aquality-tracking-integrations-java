package aquality.tracking.integrations.core.endpoints.impl;

import aquality.tracking.integrations.core.configuration.IConfiguration;
import aquality.tracking.integrations.core.http.IHttpClient;
import aquality.tracking.integrations.core.utilities.UriBuilder;
import lombok.AccessLevel;
import lombok.Getter;

public abstract class AqualityTrackingEndpoints {

    @Getter(AccessLevel.PROTECTED)
    private final IConfiguration configuration;
    @Getter(AccessLevel.PROTECTED)
    private final IHttpClient httpClient;

    protected AqualityTrackingEndpoints(IConfiguration configuration, IHttpClient httpClient) {
        this.configuration = configuration;
        this.httpClient = httpClient;
    }

    protected UriBuilder getUriBuilder(final String path) {
        return new UriBuilder(configuration.getHost()).setPath(path);
    }
}
