package aquality.tracking.integrations.core.endpoints.impl;

import aquality.tracking.integrations.core.configuration.IConfiguration;
import aquality.tracking.integrations.core.endpoints.ITestRunEndpoints;
import aquality.tracking.integrations.core.http.IHttpClient;
import aquality.tracking.integrations.core.models.TestRun;

import javax.inject.Inject;
import java.net.URI;

public class TestRunEndpoints extends AqualityTrackingEndpoints implements ITestRunEndpoints {

    private static final String START_TESTRUN_ENDPOINT = "/api/public/testrun/start";
    private static final String FINISH_TESTRUN_ENDPOINT = "/api/public/testrun/finish";

    @Inject
    public TestRunEndpoints(IConfiguration configuration, IHttpClient httpClient) {
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

        URI uri = getUriBuilder(START_TESTRUN_ENDPOINT).build();

        return getHttpClient().sendPOST(uri, testRun);
    }

    public TestRun finishTestRun(int testRunId) {
        URI uri = getUriBuilder(FINISH_TESTRUN_ENDPOINT)
                .setParameter("project_id", getConfiguration().getProjectId())
                .setParameter("id", testRunId)
                .build();

        return getHttpClient().sendGET(uri, TestRun.class);
    }
}
