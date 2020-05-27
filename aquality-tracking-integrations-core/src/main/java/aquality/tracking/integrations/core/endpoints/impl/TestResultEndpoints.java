package aquality.tracking.integrations.core.endpoints.impl;

import aquality.tracking.integrations.core.Configuration;
import aquality.tracking.integrations.core.IHttpClient;
import aquality.tracking.integrations.core.endpoints.ITestResultEndpoints;
import aquality.tracking.integrations.core.models.TestResult;
import aquality.tracking.integrations.core.utilities.JsonMapper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import javax.inject.Inject;
import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.apache.http.entity.ContentType.WILDCARD;

public class TestResultEndpoints extends AqualityTrackingEndpoints implements ITestResultEndpoints {

    private static final String START_TEST_RESULT_ENDPOINT = "/api/public/test/result/start";
    private static final String FINISH_TEST_RESULT_ENDPOINT = "/api/public/test/result/finish";
    private static final String ADD_ATTACHMENT_ENDPOINT = "/api/public/test/result/attachment";

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

        String response = getHttpClient().sendGET(uri, getDefaultHeaders().get());
        return JsonMapper.mapStringContent(response, TestResult.class);
    }

    public TestResult finishTestResult(int testResultId, int finalResultId, final String failReason) {
        TestResult testResult = new TestResult();
        testResult.setProjectId(getConfiguration().getProjectId());
        testResult.setId(testResultId);
        testResult.setFinalResultId(finalResultId);
        testResult.setFailReason(failReason);

        URI uri = buildURI(FINISH_TEST_RESULT_ENDPOINT);

        List<Header> headers = getDefaultHeaders()
                .add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                .get();

        String response = getHttpClient().sendPOST(uri, headers, JsonMapper.getJson(testResult));
        return JsonMapper.mapStringContent(response, TestResult.class);
    }

    public void addAttachment(int testResultId, final File file) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("project_id", String.valueOf(getConfiguration().getProjectId()));
        queryParams.put("test_result_id", String.valueOf(testResultId));

        URI uri = buildURI(ADD_ATTACHMENT_ENDPOINT, queryParams);

        List<Header> headers = getDefaultHeaders()
                .add(HttpHeaders.ACCEPT, WILDCARD)
                .get();

        HttpEntity postData = MultipartEntityBuilder.create()
                .addBinaryBody("files", file)
                .build();

        getHttpClient().sendPOST(uri, headers, postData);
    }
}
