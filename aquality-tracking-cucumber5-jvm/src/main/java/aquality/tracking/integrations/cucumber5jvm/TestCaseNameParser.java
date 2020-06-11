package aquality.tracking.integrations.cucumber5jvm;

import io.cucumber.core.internal.gherkin.AstBuilder;
import io.cucumber.core.internal.gherkin.Parser;
import io.cucumber.core.internal.gherkin.TokenMatcher;
import io.cucumber.core.internal.gherkin.ast.*;
import io.cucumber.plugin.event.TestCase;

import java.util.ArrayList;
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
        String scenarioName = getScenarioName(currentFeature, testCase.getName(), testCase.getTags());
        return format("%s: %s", currentFeature.getName(), scenarioName);
    }

    private Feature getCurrentFeature() {
        Parser<GherkinDocument> parser = new Parser<>(new AstBuilder());
        TokenMatcher matcher = new TokenMatcher();
        GherkinDocument gherkinDocument = parser.parse(getFileSource(testCase.getUri()), matcher);
        return gherkinDocument.getFeature();
    }

    private String getScenarioName(final Feature feature, final String testCaseName, final List<String> testCaseTags) {
        List<TableRow> examplesTableRows = getExampleTableRows(feature, testCaseName, testCaseTags);

        int tableRowIndex = IntStream.range(0, examplesTableRows.size())
                .filter(i -> examplesTableRows.get(i).getLocation().getLine() == testCase.getLine())
                .findFirst()
                .orElse(-1);

        return tableRowIndex == -1
                ? testCaseName
                : format("%s: %d", testCaseName, tableRowIndex);
    }

    private List<TableRow> getExampleTableRows(final Feature feature, final String testCaseName, final List<String> testCaseTags) {
        return feature.getChildren().stream()
                .filter(child -> child.getName().equals(testCaseName))
                .filter(child -> child instanceof ScenarioOutline)
                .map(child -> (ScenarioOutline) child)
                .filter(scenarioOutline -> hasTags(scenarioOutline, feature.getTags(), testCaseTags))
                .flatMap(child -> child.getExamples().stream())
                .flatMap(examples -> examples.getTableBody().stream())
                .collect(Collectors.toList());
    }

    private boolean hasTags(final ScenarioOutline scenarioOutline, final List<Tag> featureTags, final List<String> testCaseTags) {
        List<Tag> scenarioOutlineTags = new ArrayList<>(featureTags);
        scenarioOutlineTags.addAll(scenarioOutline.getTags());

        return scenarioOutline.getExamples().stream()
                .anyMatch(examples -> {
                    List<Tag> scenarioOutlineAndExamplesTags = new ArrayList<>(scenarioOutlineTags);
                    scenarioOutlineAndExamplesTags.addAll(examples.getTags());
                    return areTagsEqual(scenarioOutlineAndExamplesTags, testCaseTags);
                });
    }

    private boolean areTagsEqual(final List<Tag> actualTags, final List<String> expectedTags) {
        String actualTagsAsString = actualTags.stream()
                .map(Tag::getName)
                .sorted().collect(Collectors.joining());
        String expectedTagsAsString = expectedTags.stream()
                .sorted().collect(Collectors.joining());
        return actualTagsAsString.equals(expectedTagsAsString);
    }
}
