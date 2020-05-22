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

    /**
     * Sets Test Result.
     * @param testResultId  Id of Test Run to finish.
     * @param finalResultId Id of the result: {@link aquality.tracking.integrations.core.FinalResultId}
     * @param failReason    Reason of test failure. If not needed - pass null.
     * @return Instance of finished Test Result.
     */
    TestResult finishTestResult(int testResultId, int finalResultId, final String failReason);
}
