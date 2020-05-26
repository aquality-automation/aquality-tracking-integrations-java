package aquality.tracking.integrations.core.endpoints.impl;

import aquality.tracking.integrations.core.Configuration;
import aquality.tracking.integrations.core.IHttpClient;
import aquality.tracking.integrations.core.endpoints.ITestResultEndpoints;
import aquality.tracking.integrations.core.models.TestResult;
import aquality.tracking.integrations.core.utilities.JsonMapper;

import javax.inject.Inject;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class TestResultEndpoints extends AqualityTrackingEndpoints implements ITestResultEndpoints {

    private static final String START_TEST_RESULT_ENDPOINT = "/api/public/test/result/start";
    private static final String FINISH_TEST_RESULT_ENDPOINT = "/api/public/test/result/finish";

    @Inject
    protected TestResultEndpoints(Configuration configuration, IHttpClient httpClient) {
        super(configuration, httpClient);
    }

    public TestResult startTestResult(int testRunId, int testId) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("project_id", String.valueOf(getConfiguration().getProjectId()));
        queryParams.put("test_run_id", String.valueOf(testRunId));
        queryParams.put("test_id", String.valueOf(testId));

        URI uri = buildURI(START_TEST_RESULT_ENDPOINT, queryParams);
        String response = getHttpClient().sendGET(uri, getHeaders());
        return JsonMapper.mapStringContent(response, TestResult.class);
    }

    public TestResult finishTestResult(int testResultId, int finalResultId, final String failReason) {
        TestResult testResult = new TestResult();
        testResult.setProjectId(getConfiguration().getProjectId());
        testResult.setId(testResultId);
        testResult.setFinalResultId(finalResultId);
        testResult.setFailReason(failReason);

        URI uri = buildURI(FINISH_TEST_RESULT_ENDPOINT);
        String response = getHttpClient().sendPOST(uri, getHeaders(), JsonMapper.getJson(testResult));
        return JsonMapper.mapStringContent(response, TestResult.class);
    }
}
