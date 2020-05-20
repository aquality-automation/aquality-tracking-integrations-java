package aquality.tracking.integrations.cucumber5jvm;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;

public class AqualityTrackingCucumber5Jvm implements ConcurrentEventListener {

    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestRunStarted.class, testRunStartedEventHandler);
        eventPublisher.registerHandlerFor(TestCaseStarted.class, testCaseStarted);
        eventPublisher.registerHandlerFor(TestCaseFinished.class, testCaseFinished);
        eventPublisher.registerHandlerFor(TestRunFinished.class, testRunFinishedEventHandler);
    }

    private EventHandler<TestRunStarted> testRunStartedEventHandler = event -> {
        // 1. Find suite by name (get from system properties?) - store ID
        // 2. Create new test run - /api/public/testrun/start
    };

    private EventHandler<TestCaseStarted> testCaseStarted = event -> {
        // 3. Create test if not exists - /api/public/test/create-or-update - by name
        // 4. Add test to suite (the same as 3?)
        // 5. Start test result - /api/public/test/result/start
    };

    private EventHandler<TestCaseFinished> testCaseFinished = event -> {
        // 6. Finish test result - /api/public/test/result/finish
    };

    private EventHandler<TestRunFinished> testRunFinishedEventHandler = event -> {
        // 7. Finish test run - /api/public/testrun/finish
    };
}
