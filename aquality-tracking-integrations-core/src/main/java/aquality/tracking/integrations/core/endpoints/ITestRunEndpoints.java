package aquality.tracking.integrations.core.endpoints;

import aquality.tracking.integrations.core.models.TestRun;

public interface ITestRunEndpoints {
    TestRun startTestRun(int testSuiteId, final String buildName, final String environment,
                         final String executor, final String ciBuild, int debug);

    TestRun finishTestRun(int testRunId);
}
