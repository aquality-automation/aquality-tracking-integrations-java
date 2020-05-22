package aquality.tracking.integrations.cucumber5jvm;

import io.cucumber.core.internal.gherkin.ast.Feature;
import io.cucumber.core.internal.gherkin.ast.ScenarioOutline;
import io.cucumber.core.internal.gherkin.ast.TableRow;
import io.cucumber.plugin.event.TestCase;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        List<TableRow> examplesTableRows = feature.getChildren().stream()
                .filter(child -> child.getName().equals(scenarioName))
                .map(node -> (ScenarioOutline) node)
                .map(outline -> outline.getExamples().get(0))
                .flatMap(examples -> examples.getTableBody().stream())
                .collect(Collectors.toList());

        int tableRowIndex = IntStream.range(0, examplesTableRows.size())
                .filter(i -> examplesTableRows.get(i).getLocation().getLine() == testCase.getLine())
                .findFirst()
                .orElse(-1);

        String scenarioOutlineName = "";
        if (tableRowIndex != -1) {
            scenarioOutlineName = scenarioName.concat(String.valueOf(tableRowIndex));
        }
        return scenarioOutlineName;
    }
}
