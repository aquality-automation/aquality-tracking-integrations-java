package aquality.tracking.integrations.core.endpoints;

import aquality.tracking.integrations.core.AqualityException;
import aquality.tracking.integrations.core.models.TestRun;
import aquality.tracking.integrations.core.utilities.JsonMapper;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class TestRunEndpoints extends AqualityTrackingEndpoints {

    private static final String START_TESTRUN_ENDPOINT = "/api/public/testrun/start";
    private static final String FINISH_TESTRUN_ENDPOINT = "/api/public/testrun/finish";

    public TestRun startTestRun(int testSuiteId, final String buildName, final String executionEnvironment) throws AqualityException {
        TestRun testRun = new TestRun();
        testRun.setTestSuiteId(testSuiteId);
        testRun.setProjectId(CONFIG.getProjectId());
        testRun.setBuildName(buildName);
        testRun.setExecutionEnvironment(executionEnvironment);

        URI uri = buildURI(START_TESTRUN_ENDPOINT);
        String response = getHttpClient().sendPOST(uri, getHeaders(), JsonMapper.getJson(testRun));
        return JsonMapper.mapStringContent(response, TestRun.class);
    }

    public TestRun finishTestRun(int testRunId) throws AqualityException {
        Map<String, String> uriParams = new HashMap<String, String>() {{
            put("project_id", String.valueOf(CONFIG.getProjectId()));
            put("id", String.valueOf(testRunId));
        }};
        URI uri = buildURI(FINISH_TESTRUN_ENDPOINT, uriParams);
        String response = getHttpClient().sendGET(uri, getHeaders());
        return JsonMapper.mapStringContent(response, TestRun.class);
    }
}
