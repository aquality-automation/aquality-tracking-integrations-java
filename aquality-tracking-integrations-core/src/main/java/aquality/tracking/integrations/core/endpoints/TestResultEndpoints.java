package aquality.tracking.integrations.core.endpoints;

import aquality.tracking.integrations.core.AqualityException;
import aquality.tracking.integrations.core.models.TestResult;
import aquality.tracking.integrations.core.utilities.JsonMapper;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class TestResultEndpoints extends AqualityTrackingEndpoints {

    private static final String START_TEST_RESULT_ENDPOINT = "/api/public/test/result/start";
    private static final String FINISH_TEST_RESULT_ENDPOINT = "/api/public/test/result/finish";

    public TestResult startTestResult(int testRunId, int testId) throws AqualityException {
        Map<String, String> uriParams = new HashMap<String, String>() {{
            put("project_id", String.valueOf(CONFIG.getProjectId()));
            put("test_run_id", String.valueOf(testRunId));
            put("test_id", String.valueOf(testId));
        }};
        URI uri = buildURI(START_TEST_RESULT_ENDPOINT, uriParams);
        String response = getHttpClient().sendGET(uri, getHeaders());
        return JsonMapper.mapStringContent(response, TestResult.class);
    }

    public TestResult finishTestResult(int testResultId, int finalResultId, final String failReason) throws AqualityException {
        Map<String, Object> body = new HashMap<String, Object>() {{
           put("id", testResultId);
           put("project_id", CONFIG.getProjectId());
           put("final_result_id", finalResultId);
           put("fail_reason", failReason);
        }};
        URI uri = buildURI(FINISH_TEST_RESULT_ENDPOINT);
        String response = getHttpClient().sendPOST(uri, getHeaders(), JsonMapper.getJson(body));
        return JsonMapper.mapStringContent(response, TestResult.class);
    }
}
