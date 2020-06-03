package aquality.tracking.integrations.cucumber4jvm;

import aquality.tracking.integrations.core.AqualityUncheckedException;
import cucumber.api.TestCase;
import gherkin.AstBuilder;
import gherkin.Parser;
import gherkin.TokenMatcher;
import gherkin.ast.Feature;
import gherkin.ast.GherkinDocument;
import gherkin.ast.ScenarioOutline;
import gherkin.ast.TableRow;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        GherkinDocument gherkinDocument;
        try {
            String relativePathToFeatureFile = new URI(testCase.getUri()).getSchemeSpecificPart();
            Path pathToFeatureFile = Paths.get(System.getProperty("user.dir"), relativePathToFeatureFile);

            gherkinDocument = parser.parse(getFileSource(pathToFeatureFile), matcher);
        } catch (URISyntaxException e) {
            throw new AqualityUncheckedException(format("Failed to find feature file with URI: %s", testCase.getUri()), e);
        }
        return gherkinDocument.getFeature();
    }

    private String getScenarioName(final Feature feature, final String testCaseName) {
        List<TableRow> examplesTableRows = feature.getChildren().stream()
                .filter(child -> child.getName().equals(testCaseName))
                .filter(child -> child instanceof ScenarioOutline)
                .map(node -> (ScenarioOutline) node)
                .map(outline -> outline.getExamples().get(0))
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
