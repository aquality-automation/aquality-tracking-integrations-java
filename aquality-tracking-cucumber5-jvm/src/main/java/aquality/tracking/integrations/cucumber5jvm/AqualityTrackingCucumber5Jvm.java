package aquality.tracking.integrations.cucumber5jvm;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;

public class AqualityTrackingCucumber5Jvm implements ConcurrentEventListener {

    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
    }
}
