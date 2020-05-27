package aquality.tracking.integrations.core.endpoints.impl;

import aquality.tracking.integrations.core.Configuration;
import aquality.tracking.integrations.core.IHttpClient;
import aquality.tracking.integrations.core.endpoints.ITestEndpoints;
import aquality.tracking.integrations.core.models.Suite;
import aquality.tracking.integrations.core.models.Test;
import aquality.tracking.integrations.core.utilities.JsonMapper;
import org.apache.http.HttpHeaders;

import javax.inject.Inject;
import java.net.URI;
import java.util.List;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;

public class TestEndpoints extends AqualityTrackingEndpoints implements ITestEndpoints {

    private static final String CREATE_OR_UPDATE_TEST_ENDPOINT = "/api/public/test/create-or-update";

    @Inject
    protected TestEndpoints(Configuration configuration, IHttpClient httpClient) {
        super(configuration, httpClient);
    }

    public Test createOrUpdateTest(final String name, final List<Suite> suites) {
        Test test = new Test();
        test.setProjectId(getConfiguration().getProjectId());
        test.setName(name);
        test.setSuites(suites);

        URI uri = buildURI(CREATE_OR_UPDATE_TEST_ENDPOINT);

        Headers headers = getDefaultHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON);

        String response = getHttpClient().sendPOST(uri, headers.get(), JsonMapper.getJson(test));
        return JsonMapper.mapStringContent(response, Test.class);
    }
}
