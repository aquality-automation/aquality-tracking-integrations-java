package aquality.tracking.integrations.core.endpoints;

import aquality.tracking.integrations.core.models.TestResult;

public interface ITestResultEndpoints {

    /**
     * Starts new Test execution.
     * @param testRunId Id of current Test Run.
     * @param testId    Id of Test to execute.
     * @return Instance of started Test execution (Test Result).
     */
    TestResult startTestResult(int testRunId, int testId);

    TestResult finishTestResult(int testResultId, int finalResultId, final String failReason);
}
