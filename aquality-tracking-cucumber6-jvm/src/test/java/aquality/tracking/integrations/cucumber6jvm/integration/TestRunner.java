package aquality.tracking.integrations.cucumber6jvm.integration;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {
                "src/test/java/aquality/tracking/integrations/cucumber6jvm/integration/features",
        },
        glue = {
                "aquality.tracking.integrations.cucumber6jvm.integration.glue"
        },
        plugin = {
                "aquality.tracking.integrations.cucumber6jvm.AqualityTrackingCucumber6Jvm"
        },
        tags = "@Demo"
)
public class TestRunner {
}
