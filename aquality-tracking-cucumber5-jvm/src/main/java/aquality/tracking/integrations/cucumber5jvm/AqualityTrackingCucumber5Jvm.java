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
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.cucumber.core.internal.gherkin.AstBuilder;
import io.cucumber.core.internal.gherkin.Parser;
import io.cucumber.core.internal.gherkin.TokenMatcher;
import io.cucumber.core.internal.gherkin.ast.Feature;
import io.cucumber.core.internal.gherkin.ast.GherkinDocument;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;

import java.util.Collections;

public class AqualityTrackingCucumber5Jvm implements ConcurrentEventListener {

    private static TestRun currentTestRun;
    private static Suite currentSuite;

    private final ThreadLocal<Test> currentTest = new InheritableThreadLocal<>();
    private final ThreadLocal<TestResult> currentTestResult = new InheritableThreadLocal<>();
    private final ThreadLocal<Feature> currentFeature = new InheritableThreadLocal<>();

    private static final Injector INJECTOR = Guice.createInjector(new ServicesModule());

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
            eventPublisher.registerHandlerFor(TestSourceRead.class, this::handleTestSourceReadEvent);

            eventPublisher.registerHandlerFor(TestRunStarted.class, this::handleTestRunStartedEvent);
            eventPublisher.registerHandlerFor(TestRunFinished.class, this::handleTestRunFinishedEvent);

            eventPublisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStartedEvent);
            eventPublisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinishedEvent);
        }
    }

    private void handleTestSourceReadEvent(final TestSourceRead event) {
        Parser<GherkinDocument> parser = new Parser<>(new AstBuilder());
        TokenMatcher matcher = new TokenMatcher();
        GherkinDocument gherkinDocument = parser.parse(event.getSource(), matcher);
        currentFeature.set(gherkinDocument.getFeature());
    }

    private void handleTestRunStartedEvent(final TestRunStarted event) {
        currentSuite = suiteEndpoints.createSuite(configuration.getSuiteName());
        currentTestRun = testRunEndpoints.startTestRun(currentSuite.getId(), configuration.getBuildName(),
                configuration.getEnvironment(), configuration.getExecutor(),
                configuration.getCiBuild(), configuration.getDebug());
    }

    private void handleTestRunFinishedEvent(final TestRunFinished event) {
        testRunEndpoints.finishTestRun(currentTestRun.getId());
    }

    private void handleTestCaseStartedEvent(final TestCaseStarted event) {
        String testName = new TestCaseNameParser(currentFeature.get(), event.getTestCase()).getScenarioName();
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
    }
}
