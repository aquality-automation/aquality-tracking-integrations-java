package aquality.tracking.integrations.cucumber7jvm;

import aquality.tracking.integrations.core.AqualityTrackingLifecycle;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EmbedEvent;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestCaseFinished;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestRunStarted;

public class AqualityTrackingCucumber7Jvm implements ConcurrentEventListener {

    private final AqualityTrackingLifecycle lifecycle;

    public AqualityTrackingCucumber7Jvm() {
        lifecycle = new AqualityTrackingLifecycle();
    }

    @Override
    public void setEventPublisher(final EventPublisher eventPublisher) {
        if (lifecycle.isEnabled()) {
            eventPublisher.registerHandlerFor(TestRunStarted.class, this::handleTestRunStartedEvent);
            eventPublisher.registerHandlerFor(TestRunFinished.class, this::handleTestRunFinishedEvent);

            eventPublisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStartedEvent);
            eventPublisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinishedEvent);

            eventPublisher.registerHandlerFor(EmbedEvent.class, this::handleEmbedEvent);
        }
    }

    private void handleTestRunStartedEvent(final TestRunStarted event) {
        lifecycle.startTestRun();
    }

    private void handleTestRunFinishedEvent(final TestRunFinished event) {
        lifecycle.finishTestRun();
    }

    private void handleTestCaseStartedEvent(final TestCaseStarted event) {
        String testName = new TestCaseNameParser(event.getTestCase()).parse();
        lifecycle.startTestExecution(testName);
    }

    private void handleTestCaseFinishedEvent(final TestCaseFinished event) {
        TestCaseResultParser testCaseResultParser = new TestCaseResultParser(event.getResult());
        TestCaseResultParser.TestCaseResult testCaseResult = testCaseResultParser.parse();
        lifecycle.finishTestExecution(testCaseResult.getFinalResultId(), testCaseResult.getFailReason());
    }

    private void handleEmbedEvent(final EmbedEvent event) {
        String fileName = String.format("%s_%s", event.getTestCase().getId(), event.getName());
        lifecycle.addAttachment(fileName, event.getData());
    }
}
