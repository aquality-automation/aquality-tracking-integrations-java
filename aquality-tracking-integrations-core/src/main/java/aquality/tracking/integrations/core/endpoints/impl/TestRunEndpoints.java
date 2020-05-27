package aquality.tracking.integrations.core.endpoints.impl;

import aquality.tracking.integrations.core.Configuration;
import aquality.tracking.integrations.core.IHttpClient;
import aquality.tracking.integrations.core.endpoints.ITestRunEndpoints;
import aquality.tracking.integrations.core.models.TestRun;
import aquality.tracking.integrations.core.utilities.JsonMapper;
import org.apache.http.HttpHeaders;

import javax.inject.Inject;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;

public class TestRunEndpoints extends AqualityTrackingEndpoints implements ITestRunEndpoints {

    private static final String START_TESTRUN_ENDPOINT = "/api/public/testrun/start";
    private static final String FINISH_TESTRUN_ENDPOINT = "/api/public/testrun/finish";

    @Inject
    protected TestRunEndpoints(Configuration configuration, IHttpClient httpClient) {
        super(configuration, httpClient);
    }

    public TestRun startTestRun(int testSuiteId, final String buildName, final String environment,
                                final String executor, final String ciBuild, boolean debug) {
        TestRun testRun = new TestRun();
        testRun.setTestSuiteId(testSuiteId);
        testRun.setProjectId(getConfiguration().getProjectId());
        testRun.setBuildName(buildName);
        testRun.setExecutionEnvironment(environment);
        testRun.setAuthor(executor);
        testRun.setCiBuild(ciBuild);
        testRun.setDebug(debug ? 1 : 0);

        URI uri = buildURI(START_TESTRUN_ENDPOINT);

        Headers headers = getDefaultHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON);

        String response = getHttpClient().sendPOST(uri, headers.get(), JsonMapper.getJson(testRun));
        return JsonMapper.mapStringContent(response, TestRun.class);
    }

    public TestRun finishTestRun(int testRunId) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("project_id", String.valueOf(getConfiguration().getProjectId()));
        queryParams.put("id", String.valueOf(testRunId));

        URI uri = buildURI(FINISH_TESTRUN_ENDPOINT, queryParams);

        String response = getHttpClient().sendGET(uri, getDefaultHeaders().get());
        return JsonMapper.mapStringContent(response, TestRun.class);
    }
}
