package aquality.tracking.integrations.core;

import aquality.tracking.integrations.core.configuration.IConfiguration;
import aquality.tracking.integrations.core.endpoints.ISuiteEndpoints;
import aquality.tracking.integrations.core.endpoints.ITestEndpoints;
import aquality.tracking.integrations.core.endpoints.ITestResultEndpoints;
import aquality.tracking.integrations.core.endpoints.ITestRunEndpoints;
import aquality.tracking.integrations.core.models.Suite;
import aquality.tracking.integrations.core.models.Test;
import aquality.tracking.integrations.core.models.TestResult;
import aquality.tracking.integrations.core.models.TestRun;
import aquality.tracking.integrations.core.utilities.FileUtils;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.File;
import java.nio.file.Paths;
import java.util.Collections;

public class AqualityTrackingLifecycle {

    private static final Injector INJECTOR = Guice.createInjector(new ServicesModule());

    private static TestRun currentTestRun;
    private static Suite currentSuite;

    private final ThreadLocal<Test> currentTest = new ThreadLocal<>();
    private final ThreadLocal<TestResult> currentTestResult = new ThreadLocal<>();

    private final IConfiguration configuration;
    private final ISuiteEndpoints suiteEndpoints;
    private final ITestRunEndpoints testRunEndpoints;
    private final ITestEndpoints testEndpoints;
    private final ITestResultEndpoints testResultEndpoints;

    public AqualityTrackingLifecycle() {
        configuration = INJECTOR.getInstance(IConfiguration.class);
        suiteEndpoints = INJECTOR.getInstance(ISuiteEndpoints.class);
        testRunEndpoints = INJECTOR.getInstance(ITestRunEndpoints.class);
        testEndpoints = INJECTOR.getInstance(ITestEndpoints.class);
        testResultEndpoints = INJECTOR.getInstance(ITestResultEndpoints.class);
    }

    public boolean isEnabled() {
        return configuration.isEnabled();
    }

    public void startTestRun() {
        Suite suite = suiteEndpoints.createSuite(configuration.getSuiteName());
        TestRun testRun = testRunEndpoints.startTestRun(suite.getId(), configuration.getBuildName(),
                configuration.getEnvironment(), configuration.getExecutor(),
                configuration.getCiBuild(), configuration.isDebug());
        setCurrentSuite(suite);
        setCurrentTestRun(testRun);
    }

    private static void setCurrentSuite(final Suite suite) {
        currentSuite = suite;
    }

    private static void setCurrentTestRun(final TestRun testRun) {
        currentTestRun = testRun;
    }

    public void startTestExecution(final String testName) {
        Test test = testEndpoints.createOrUpdateTest(testName, Collections.singletonList(currentSuite));
        currentTest.set(test);
        TestResult testResult = testResultEndpoints.startTestResult(currentTestRun.getId(), currentTest.get().getId());
        currentTestResult.set(testResult);
    }

    public void addAttachment(final String fileName, final byte[] attachmentData) {
        String filePath = Paths.get(configuration.getAttachmentsDirectory(), fileName).toString();
        File attachmentFile = FileUtils.writeToFile(filePath, attachmentData);
        testResultEndpoints.addAttachment(currentTestResult.get().getId(), attachmentFile);
    }

    public void finishTestExecution(final int finalResultId, final String failReason) {
        testResultEndpoints.finishTestResult(currentTestResult.get().getId(), finalResultId, failReason);
        currentTest.remove();
        currentTestResult.remove();
    }

    public void finishTestRun() {
        testRunEndpoints.finishTestRun(currentTestRun.getId());
    }
}
