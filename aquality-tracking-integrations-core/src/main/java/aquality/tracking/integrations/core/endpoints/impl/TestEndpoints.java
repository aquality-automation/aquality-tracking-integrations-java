package aquality.tracking.integrations.core.endpoints.impl;

import aquality.tracking.integrations.core.configuration.IConfiguration;
import aquality.tracking.integrations.core.endpoints.ITestEndpoints;
import aquality.tracking.integrations.core.http.IHttpClient;
import aquality.tracking.integrations.core.models.Suite;
import aquality.tracking.integrations.core.models.Test;

import javax.inject.Inject;
import java.net.URI;
import java.util.List;

public class TestEndpoints extends AqualityTrackingEndpoints implements ITestEndpoints {

    private static final String CREATE_OR_UPDATE_TEST_ENDPOINT = "/api/public/test/create-or-update";

    @Inject
    public TestEndpoints(IConfiguration configuration, IHttpClient httpClient) {
        super(configuration, httpClient);
    }

    public Test createOrUpdateTest(final String name, final List<Suite> suites) {
        Test test = new Test();
        test.setProjectId(getConfiguration().getProjectId());
        test.setName(name);
        test.setSuites(suites);

        URI uri = getUriBuilder(CREATE_OR_UPDATE_TEST_ENDPOINT).build();

        return getHttpClient().sendPOST(uri, test);
    }
}
