package aquality.tracking.integrations.core.endpoints.impl;

import aquality.tracking.integrations.core.configuration.IConfiguration;
import aquality.tracking.integrations.core.endpoints.ITestResultEndpoints;
import aquality.tracking.integrations.core.http.IHttpClient;
import aquality.tracking.integrations.core.models.TestResult;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import javax.inject.Inject;
import java.io.File;
import java.net.URI;

public class TestResultEndpoints extends AqualityTrackingEndpoints implements ITestResultEndpoints {

    private static final String START_TEST_RESULT_ENDPOINT = "/api/public/test/result/start";
    private static final String FINISH_TEST_RESULT_ENDPOINT = "/api/public/test/result/finish";
    private static final String ADD_ATTACHMENT_ENDPOINT = "/api/public/test/result/attachment";

    @Inject
    public TestResultEndpoints(IConfiguration configuration, IHttpClient httpClient) {
        super(configuration, httpClient);
    }

    public TestResult startTestResult(int testRunId, int testId) {
        URI uri = getUriBuilder(START_TEST_RESULT_ENDPOINT)
                .setParameter("project_id", getConfiguration().getProjectId())
                .setParameter("test_run_id", testRunId)
                .setParameter("test_id", testId)
                .build();

        return getHttpClient().sendGET(uri, TestResult.class);
    }

    public TestResult finishTestResult(int testResultId, int finalResultId, final String failReason) {
        TestResult testResult = new TestResult();
        testResult.setProjectId(getConfiguration().getProjectId());
        testResult.setId(testResultId);
        testResult.setFinalResultId(finalResultId);
        testResult.setFailReason(failReason);

        URI uri = getUriBuilder(FINISH_TEST_RESULT_ENDPOINT).build();

        return getHttpClient().sendPOST(uri, testResult);
    }

    public void addAttachment(int testResultId, final File file) {
        URI uri = getUriBuilder(ADD_ATTACHMENT_ENDPOINT)
                .setParameter("project_id", getConfiguration().getProjectId())
                .setParameter("test_result_id", testResultId)
                .build();

        HttpEntity postData = MultipartEntityBuilder.create()
                .addBinaryBody("files", file)
                .build();

        getHttpClient().sendPOST(uri, postData);
    }
}
