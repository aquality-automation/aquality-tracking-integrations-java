package aquality.tracking.integrations.cucumber5jvm;

import aquality.tracking.integrations.core.AqualityUncheckedException;
import io.cucumber.core.internal.gherkin.ast.Examples;
import io.cucumber.core.internal.gherkin.ast.Feature;
import io.cucumber.core.internal.gherkin.ast.ScenarioOutline;
import io.cucumber.core.internal.gherkin.ast.TableRow;
import io.cucumber.plugin.event.TestCase;

import java.util.List;
import java.util.stream.IntStream;

import static java.lang.String.format;

class TestCaseNameParser {

    private static final String SCENARIO_OUTLINE_KEYWORD = "Scenario Outline";

    private final Feature feature;
    private final TestCase testCase;

    TestCaseNameParser(Feature feature, TestCase testCase) {
        this.feature = feature;
        this.testCase = testCase;
    }

    public String getScenarioName() {
        String scenarioName = testCase.getName();
        if (testCase.getKeyword().equals(SCENARIO_OUTLINE_KEYWORD)) {
            scenarioName = getScenarioOutlineName(scenarioName);
        }
        return feature.getName().concat(scenarioName);
    }

    private String getScenarioOutlineName(final String scenarioName) {
        final Examples examples = feature.getChildren().stream()
                .filter(child -> child.getName().equals(scenarioName))
                .map(node -> (ScenarioOutline) node)
                .map(outline -> outline.getExamples().get(0))
                .findFirst()
                .orElseThrow(() -> new AqualityUncheckedException(format("Examples for scenario '%s' not found",
                        scenarioName)));

        int currentExampleLine = testCase.getLine();
        final List<TableRow> examplesTableRows = examples.getTableBody();
        int tableRowIndex = IntStream.range(0, examplesTableRows.size())
                .filter(i -> examplesTableRows.get(i).getLocation().getLine() == currentExampleLine)
                .findFirst()
                .orElseThrow(() -> new AqualityUncheckedException(format("Example for scenario '%s' at line '%d' not found",
                        scenarioName, currentExampleLine)));

        return scenarioName.concat(String.valueOf(tableRowIndex));
    }
}
