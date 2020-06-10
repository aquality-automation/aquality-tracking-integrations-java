package aquality.tracking.integrations.cucumber5jvm.integration.glue;

import io.cucumber.java.en.Given;

public class Steps {

    @Given("I pass this scenario")
    public void passThisScenario() {
    }

    @Given("I pass this scenario with parameter {}")
    public void passThisScenario(String parameter) {
    }
}
