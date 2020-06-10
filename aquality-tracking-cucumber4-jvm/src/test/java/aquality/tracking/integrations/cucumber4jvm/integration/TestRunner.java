package aquality.tracking.integrations.cucumber4jvm.integration;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {
                "src/test/java/aquality/tracking/integrations/cucumber4jvm/integration/features",
        },
        glue = {
                "aquality.tracking.integrations.cucumber4jvm.integration.glue"
        },
        plugin = {
                "aquality.tracking.integrations.cucumber4jvm.AqualityTrackingCucumber4Jvm"
        },
        strict = true
)
public class TestRunner {
}
