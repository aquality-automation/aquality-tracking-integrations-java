package aquality.tracking.integrations.core.endpoints.impl;

import aquality.tracking.integrations.core.Configuration;
import aquality.tracking.integrations.core.IHttpClient;
import aquality.tracking.integrations.core.endpoints.ITestEndpoints;
import aquality.tracking.integrations.core.models.Suite;
import aquality.tracking.integrations.core.models.Test;
import aquality.tracking.integrations.core.utilities.JsonMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.inject.Inject;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestEndpoints extends AqualityTrackingEndpoints implements ITestEndpoints {

    private static final String TEST_ENDPOINT = "/api/test";

    @Inject
    protected TestEndpoints(Configuration configuration, IHttpClient httpClient) {
        super(configuration, httpClient);
    }

    public Test createOrUpdateTest(final String name, final List<Suite> suites) {
        List<Test> foundTests = findTest(name);
        if (foundTests.isEmpty()) {
            return createOrUpdateTest(null, name, suites);
        } else {
            Test foundTest = foundTests.get(0);
            List<Suite> testSuites = foundTest.getSuites();
            testSuites.addAll(suites);
            return createOrUpdateTest(foundTest.getId(), foundTest.getName(), testSuites);
        }
    }

    private List<Test> findTest(final String name) {
        Map<String, String> queryParams = new HashMap<String, String>() {{
            put("name", name);
            put("project_id", String.valueOf(getConfiguration().getProjectId()));
        }};

        URI uri = buildURI(TEST_ENDPOINT, queryParams);
        String response = getHttpClient().sendGET(uri, getHeaders());
        return JsonMapper.mapStringContent(response, new TypeReference<List<Test>>() {});
    }

    private Test createOrUpdateTest(final Integer id, final String name, final List<Suite> suites) {
        Test test = new Test();
        test.setId(id);
        test.setProjectId(getConfiguration().getProjectId());
        test.setName(name);
        test.setSuites(suites);

        URI uri = buildURI(TEST_ENDPOINT);
        String response = getHttpClient().sendPOST(uri, getHeaders(), JsonMapper.getJson(test));
        return JsonMapper.mapStringContent(response, Test.class);
    }
}
