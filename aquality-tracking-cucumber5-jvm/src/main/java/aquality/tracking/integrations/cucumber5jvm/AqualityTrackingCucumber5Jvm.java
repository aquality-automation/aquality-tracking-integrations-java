package aquality.tracking.integrations.cucumber5jvm;

import aquality.tracking.integrations.core.Configuration;
import aquality.tracking.integrations.core.ServicesModule;
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
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;

import java.io.File;
import java.nio.file.Paths;
import java.util.Collections;

import static java.lang.String.format;

public class AqualityTrackingCucumber5Jvm implements ConcurrentEventListener {

    private static final Injector INJECTOR = Guice.createInjector(new ServicesModule());

    private static TestRun currentTestRun;
    private static Suite currentSuite;

    private final ThreadLocal<Test> currentTest = new ThreadLocal<>();
    private final ThreadLocal<TestResult> currentTestResult = new ThreadLocal<>();

    private final Configuration configuration;
    private final ISuiteEndpoints suiteEndpoints;
    private final ITestRunEndpoints testRunEndpoints;
    private final ITestEndpoints testEndpoints;
    private final ITestResultEndpoints testResultEndpoints;

    public AqualityTrackingCucumber5Jvm() {
        configuration = INJECTOR.getInstance(Configuration.class);
        suiteEndpoints = INJECTOR.getInstance(ISuiteEndpoints.class);
        testRunEndpoints = INJECTOR.getInstance(ITestRunEndpoints.class);
        testEndpoints = INJECTOR.getInstance(ITestEndpoints.class);
        testResultEndpoints = INJECTOR.getInstance(ITestResultEndpoints.class);
    }

    @Override
    public void setEventPublisher(final EventPublisher eventPublisher) {
        if (configuration.isEnabled()) {
            eventPublisher.registerHandlerFor(TestRunStarted.class, this::handleTestRunStartedEvent);
            eventPublisher.registerHandlerFor(TestRunFinished.class, this::handleTestRunFinishedEvent);

            eventPublisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStartedEvent);
            eventPublisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinishedEvent);

            eventPublisher.registerHandlerFor(EmbedEvent.class, this::handleEmbedEvent);
        }
    }

    private void handleTestRunStartedEvent(final TestRunStarted event) {
        currentSuite = suiteEndpoints.createSuite(configuration.getSuiteName());
        currentTestRun = testRunEndpoints.startTestRun(currentSuite.getId(), configuration.getBuildName(),
                configuration.getEnvironment(), configuration.getExecutor(),
                configuration.getCiBuild(), configuration.isDebug());
    }

    private void handleTestRunFinishedEvent(final TestRunFinished event) {
        testRunEndpoints.finishTestRun(currentTestRun.getId());
    }

    private void handleTestCaseStartedEvent(final TestCaseStarted event) {
        String testName = new TestCaseNameParser(event.getTestCase()).getScenarioName();
        Test test = testEndpoints.createOrUpdateTest(testName, Collections.singletonList(currentSuite));
        currentTest.set(test);
        TestResult testResult = testResultEndpoints.startTestResult(currentTestRun.getId(), currentTest.get().getId());
        currentTestResult.set(testResult);
    }

    private void handleTestCaseFinishedEvent(final TestCaseFinished event) {
        TestCaseResultParser testCaseResultParser = new TestCaseResultParser(event.getResult());
        TestCaseResultParser.TestCaseResult testCaseResult = testCaseResultParser.parse();
        testResultEndpoints.finishTestResult(currentTestResult.get().getId(),
                testCaseResult.getFinalResultId(), testCaseResult.getFailReason());
        currentTest.remove();
        currentTestResult.remove();
    }

    private void handleEmbedEvent(final EmbedEvent event) {
        String filePath = Paths.get(configuration.getAttachmentsDirectory(),
                format("%s_%s", event.getTestCase().getId(), event.getName())).toString();
        File attachmentFile = FileUtils.writeToFile(filePath, event.getData());
        testResultEndpoints.addAttachment(currentTestResult.get().getId(), attachmentFile);
    }
}
