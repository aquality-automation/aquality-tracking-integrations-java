package aquality.tracking.integrations.cucumber6jvm.integration.glue;

import io.cucumber.java.en.Given;

public class Steps {

    @Given("I pass this scenario")
    public void passThisScenario() {
        // step logic
    }

    @Given("I pass this scenario with parameter {}")
    public void passThisScenario(String parameter) {
        // step logic
    }
}
