package aquality.tracking.integrations.cucumber5jvm;

import aquality.tracking.integrations.core.endpoints.SuiteEndpoints;
import aquality.tracking.integrations.core.endpoints.TestEndpoints;
import aquality.tracking.integrations.core.endpoints.TestResultEndpoints;
import aquality.tracking.integrations.core.endpoints.TestRunEndpoints;
import aquality.tracking.integrations.core.models.Suite;
import aquality.tracking.integrations.core.models.Test;
import aquality.tracking.integrations.core.models.TestResult;
import aquality.tracking.integrations.core.models.TestRun;
import io.cucumber.core.internal.gherkin.AstBuilder;
import io.cucumber.core.internal.gherkin.Parser;
import io.cucumber.core.internal.gherkin.TokenMatcher;
import io.cucumber.core.internal.gherkin.ast.Feature;
import io.cucumber.core.internal.gherkin.ast.GherkinDocument;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;

import java.util.Collections;
import java.util.List;

public class AqualityTrackingCucumber5Jvm implements ConcurrentEventListener {

    private static final String SUITE_NAME = "Test Suite"; // TODO: get from properties
    private static final String BUILD_NAME = "New build"; // TODO: get from properties
    private static final String ENV = "Test"; // TODO: get from properties
    private static final String EXECUTOR = "CI"; // TODO: get from properties
    private static final String CI_BUILD = "LINK_TO_CI_BUILD"; // TODO: get from properties
    private static final int DEBUG = 0; // TODO: get from properties

    private static TestRun currentTestRun;
    private static Suite currentSuite;

    private final ThreadLocal<Test> currentTest = new InheritableThreadLocal<>();
    private final ThreadLocal<TestResult> currentTestResult = new InheritableThreadLocal<>();
    private final ThreadLocal<Feature> currentFeature = new InheritableThreadLocal<>();

    private final SuiteEndpoints suiteEndpoints;
    private final TestRunEndpoints testRunEndpoints;
    private final TestEndpoints testEndpoints;
    private final TestResultEndpoints testResultEndpoints;

    public AqualityTrackingCucumber5Jvm() {
        suiteEndpoints = new SuiteEndpoints();
        testRunEndpoints = new TestRunEndpoints();
        testEndpoints = new TestEndpoints();
        testResultEndpoints = new TestResultEndpoints();
    }

    @Override
    public void setEventPublisher(final EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestSourceRead.class, this::handleTestSourceReadEvent);

        eventPublisher.registerHandlerFor(TestRunStarted.class, this::handleTestRunStartedEvent);
        eventPublisher.registerHandlerFor(TestRunFinished.class, this::handleTestRunFinishedEvent);

        eventPublisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStartedEvent);
        eventPublisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinishedEvent);
    }

    private void handleTestSourceReadEvent(final TestSourceRead event) {
        Parser<GherkinDocument> parser = new Parser<>(new AstBuilder());
        TokenMatcher matcher = new TokenMatcher();
        GherkinDocument gherkinDocument = parser.parse(event.getSource(), matcher);
        currentFeature.set(gherkinDocument.getFeature());
    }

    private void handleTestRunStartedEvent(final TestRunStarted event) {
        currentSuite = suiteEndpoints.createSuite(SUITE_NAME);
        currentTestRun = testRunEndpoints.startTestRun(currentSuite.getId(), BUILD_NAME, ENV, EXECUTOR, CI_BUILD, DEBUG);
    }

    private void handleTestRunFinishedEvent(final TestRunFinished event) {
        testRunEndpoints.finishTestRun(currentTestRun.getId());
    }

    private void handleTestCaseStartedEvent(final TestCaseStarted event) {
        final String testName = new TestCaseNameParser(currentFeature.get(), event.getTestCase()).getScenarioName();
        final List<Test> foundTests = testEndpoints.findTest(testName);
        if (foundTests.isEmpty()) {
            Test newTest = testEndpoints.createTest(testName, Collections.singletonList(currentSuite));
            currentTest.set(newTest);
        } else {
            Test foundTest = foundTests.get(0);
            List<Suite> testSuites = foundTest.getSuites();
            testSuites.add(currentSuite);
            Test test = testEndpoints.updateTest(foundTest.getId(), foundTest.getName(), testSuites);
            currentTest.set(test);
        }
        final TestResult testResult = testResultEndpoints.startTestResult(currentTestRun.getId(), currentTest.get().getId());
        currentTestResult.set(testResult);
    }

    private void handleTestCaseFinishedEvent(final TestCaseFinished event) {
        TestCaseResultParser testCaseResultParser = new TestCaseResultParser(event.getResult());
        TestCaseResultParser.TestCaseResult testCaseResult = testCaseResultParser.parse();
        testResultEndpoints.finishTestResult(currentTestResult.get().getId(),
                testCaseResult.getFinalResultId(), testCaseResult.getFailReason());
    }
}
