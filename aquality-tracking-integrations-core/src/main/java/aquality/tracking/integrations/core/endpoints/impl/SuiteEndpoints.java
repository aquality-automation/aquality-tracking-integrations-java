package aquality.tracking.integrations.core.endpoints.impl;

import aquality.tracking.integrations.core.configuration.IConfiguration;
import aquality.tracking.integrations.core.endpoints.ISuiteEndpoints;
import aquality.tracking.integrations.core.http.IHttpClient;
import aquality.tracking.integrations.core.models.Suite;
import aquality.tracking.integrations.core.utilities.JsonMapper;

import javax.inject.Inject;
import java.net.URI;

public class SuiteEndpoints extends AqualityTrackingEndpoints implements ISuiteEndpoints {

    private static final String CREATE_OR_UPDATE_ENDPOINT = "/api/public/suite/create-or-update";

    @Inject
    public SuiteEndpoints(IConfiguration configuration, IHttpClient httpClient) {
        super(configuration, httpClient);
    }

    public Suite createSuite(final String name) {
        Suite suite = new Suite();
        suite.setProjectId(getConfiguration().getProjectId());
        suite.setName(name);

        URI uri = getUriBuilder(CREATE_OR_UPDATE_ENDPOINT).build();

        String response = getHttpClient().sendPOST(uri, JsonMapper.getJson(suite));
        return JsonMapper.mapStringContent(response, Suite.class);
    }
}
