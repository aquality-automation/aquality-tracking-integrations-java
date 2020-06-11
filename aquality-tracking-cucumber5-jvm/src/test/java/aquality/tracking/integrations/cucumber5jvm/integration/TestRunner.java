package aquality.tracking.integrations.cucumber5jvm.integration;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {
                "src/test/java/aquality/tracking/integrations/cucumber5jvm/integration/features",
        },
        glue = {
                "aquality.tracking.integrations.cucumber5jvm.integration.glue"
        },
        plugin = {
                "aquality.tracking.integrations.cucumber5jvm.AqualityTrackingCucumber5Jvm"
        },
        tags = {
                "@Demo"
        },
        strict = true
)
public class TestRunner {
}
