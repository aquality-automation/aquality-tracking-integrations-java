package aquality.tracking.integrations.cucumber5jvm;

import io.cucumber.core.internal.gherkin.AstBuilder;
import io.cucumber.core.internal.gherkin.Parser;
import io.cucumber.core.internal.gherkin.TokenMatcher;
import io.cucumber.core.internal.gherkin.ast.Feature;
import io.cucumber.core.internal.gherkin.ast.GherkinDocument;
import io.cucumber.core.internal.gherkin.ast.ScenarioOutline;
import io.cucumber.core.internal.gherkin.ast.TableRow;
import io.cucumber.plugin.event.TestCase;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static aquality.tracking.integrations.core.utilities.FileUtils.getFileSource;
import static java.lang.String.format;

class TestCaseNameParser {

    private final TestCase testCase;

    TestCaseNameParser(TestCase testCase) {
        this.testCase = testCase;
    }

    public String parse() {
        Feature currentFeature = getCurrentFeature();
        String scenarioName = getScenarioName(currentFeature, testCase.getName());
        return format("%s: %s", currentFeature.getName(), scenarioName);
    }

    private Feature getCurrentFeature() {
        Parser<GherkinDocument> parser = new Parser<>(new AstBuilder());
        TokenMatcher matcher = new TokenMatcher();
        GherkinDocument gherkinDocument = parser.parse(getFileSource(testCase.getUri()), matcher);
        return gherkinDocument.getFeature();
    }

    private String getScenarioName(final Feature feature, final String testCaseName) {
        List<TableRow> examplesTableRows = feature.getChildren().stream()
                .filter(child -> child.getName().equals(testCaseName))
                .filter(child -> child instanceof ScenarioOutline)
                .map(node -> ((ScenarioOutline) node).getExamples().get(0))
                .flatMap(examples -> examples.getTableBody().stream())
                .collect(Collectors.toList());

        int tableRowIndex = IntStream.range(0, examplesTableRows.size())
                .filter(i -> examplesTableRows.get(i).getLocation().getLine() == testCase.getLine())
                .findFirst()
                .orElse(-1);

        return tableRowIndex == -1
                ? testCaseName
                : format("%s: %d", testCaseName, tableRowIndex);
    }
}
