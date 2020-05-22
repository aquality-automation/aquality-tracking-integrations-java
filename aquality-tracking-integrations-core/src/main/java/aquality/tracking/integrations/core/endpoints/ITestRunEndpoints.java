package aquality.tracking.integrations.core.endpoints;

import aquality.tracking.integrations.core.models.TestRun;

public interface ITestRunEndpoints {

    /**
     * Starts new Test Run.
     * @param testSuiteId Id of Test Suite which included in Test Run.
     * @param buildName   Name of current build.
     * @param environment Name of execution environment.
     * @param executor    Name of author.
     * @param ciBuild     Link to the CI build of current Test Run.
     * @param debug       Debug (1) or regular (0) execution. If debug Test Run won't be present in Aquality Tracking.
     * @return Instance of started Test Run.
     */
    TestRun startTestRun(int testSuiteId, final String buildName, final String environment,
                         final String executor, final String ciBuild, int debug);

    /**
     * Finished Test Run.
     * @param testRunId Id of Test Run which should be finished.
     * @return Instance of finished Test Run.
     */
    TestRun finishTestRun(int testRunId);
}
