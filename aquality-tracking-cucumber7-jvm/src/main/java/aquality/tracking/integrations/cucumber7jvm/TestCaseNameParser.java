package aquality.tracking.integrations.cucumber7jvm;

import aquality.tracking.integrations.core.AqualityUncheckedException;
import io.cucumber.gherkin.GherkinParser;
import io.cucumber.messages.types.Envelope;
import io.cucumber.messages.types.Feature;
import io.cucumber.messages.types.GherkinDocument;
import io.cucumber.messages.types.Scenario;
import io.cucumber.messages.types.TableRow;
import io.cucumber.messages.types.Tag;
import io.cucumber.plugin.event.TestCase;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class TestCaseNameParser {

    private final TestCase testCase;

    TestCaseNameParser(TestCase testCase) {
        this.testCase = testCase;
    }

    public String parse() {
        Feature currentFeature = getCurrentFeature();
        String scenarioName = getScenarioName(currentFeature, testCase.getName(), testCase.getTags());
        return String.format(
                "%s: %s",
                currentFeature != null ? currentFeature.getName() : StringUtils.EMPTY,
                scenarioName);
    }

    private Feature getCurrentFeature() {
        GherkinParser parser = GherkinParser.builder().build();
        Path filePath = Paths.get(testCase.getUri());
        try {
            return parser
                    .parse(filePath)
                    .filter(envelope -> envelope.getGherkinDocument().isPresent())
                    .findFirst()
                    .flatMap(Envelope::getGherkinDocument)
                    .flatMap(GherkinDocument::getFeature)
                    .orElse(null);
        } catch (IOException e) {
            throw new AqualityUncheckedException(String.format("File %s not found.", filePath), e);
        }
    }

    private String getScenarioName(final Feature feature, final String testCaseName, final List<String> testCaseTags) {
        List<TableRow> examplesTableRows = getExampleTableRows(feature, testCaseName, testCaseTags);

        int tableRowIndex = IntStream.range(0, examplesTableRows.size())
                .filter(i -> examplesTableRows.get(i).getLocation().getLine().equals((long)testCase.getLocation().getLine()))
                .findFirst()
                .orElse(-1);

        return tableRowIndex == -1 ? testCaseName : String.format("%s: %d", testCaseName, tableRowIndex);
    }

    private List<TableRow> getExampleTableRows(
            final Feature feature,
            final String testCaseName,
            final List<String> testCaseTags) {
        if (feature == null) {
            return new ArrayList<>();
        }

        return feature.getChildren().stream()
                .filter(child -> child.getScenario().isPresent())
                .map(child -> child.getScenario().get())
                .filter(scenario -> scenario.getName().equals(testCaseName) &&
                        !scenario.getExamples().isEmpty() &&
                        hasTags(scenario, feature.getTags(), testCaseTags))
                .flatMap(scenarioOutline -> scenarioOutline.getExamples().stream())
                .flatMap(examples -> examples.getTableBody().stream())
                .collect(Collectors.toList());
    }

    private boolean hasTags(final Scenario scenarioOutline, final List<Tag> featureTags, final List<String> testCaseTags) {
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