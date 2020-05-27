package aquality.tracking.integrations.core.endpoints.impl;

import aquality.tracking.integrations.core.Configuration;
import aquality.tracking.integrations.core.IHttpClient;
import aquality.tracking.integrations.core.endpoints.ISuiteEndpoints;
import aquality.tracking.integrations.core.models.Suite;
import aquality.tracking.integrations.core.utilities.JsonMapper;
import org.apache.http.HttpHeaders;

import javax.inject.Inject;
import java.net.URI;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;

public class SuiteEndpoints extends AqualityTrackingEndpoints implements ISuiteEndpoints {

    private static final String CREATE_OR_UPDATE_ENDPOINT = "/api/public/suite/create-or-update";

    @Inject
    protected SuiteEndpoints(Configuration configuration, IHttpClient httpClient) {
        super(configuration, httpClient);
    }

    public Suite createSuite(final String name) {
        Suite suite = new Suite();
        suite.setProjectId(getConfiguration().getProjectId());
        suite.setName(name);

        URI uri = buildURI(CREATE_OR_UPDATE_ENDPOINT);

        Headers headers = getDefaultHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON);

        String response = getHttpClient().sendPOST(uri, headers.get(), JsonMapper.getJson(suite));
        return JsonMapper.mapStringContent(response, Suite.class);
    }
}
