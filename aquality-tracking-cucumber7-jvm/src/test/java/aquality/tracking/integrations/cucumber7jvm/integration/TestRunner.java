package aquality.tracking.integrations.cucumber7jvm.integration;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {
                "src/test/java/aquality/tracking/integrations/cucumber7jvm/integration/features",
        },
        glue = {
                "aquality.tracking.integrations.cucumber7jvm.integration.glue"
        },
        plugin = {
                "aquality.tracking.integrations.cucumber7jvm.AqualityTrackingCucumber7Jvm"
        },
        tags = "@Demo"
)
public class TestRunner {
}
