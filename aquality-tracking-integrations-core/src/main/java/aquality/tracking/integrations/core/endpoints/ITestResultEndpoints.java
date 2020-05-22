package aquality.tracking.integrations.core.endpoints;

import aquality.tracking.integrations.core.models.TestResult;

public interface ITestResultEndpoints {
    TestResult startTestResult(int testRunId, int testId);

    TestResult finishTestResult(int testResultId, int finalResultId, final String failReason);
}
