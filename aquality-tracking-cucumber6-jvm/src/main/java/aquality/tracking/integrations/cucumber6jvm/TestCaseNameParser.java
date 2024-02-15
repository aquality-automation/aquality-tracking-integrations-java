package aquality.tracking.integrations.cucumber6jvm;

import aquality.tracking.integrations.core.utilities.FileUtils;
import io.cucumber.gherkin.Gherkin;
import io.cucumber.messages.Messages;
import io.cucumber.messages.Messages.Envelope;
import io.cucumber.messages.Messages.GherkinDocument;
import io.cucumber.messages.Messages.GherkinDocument.Feature;
import io.cucumber.messages.Messages.GherkinDocument.Feature.Scenario;
import io.cucumber.messages.Messages.GherkinDocument.Feature.TableRow;
import io.cucumber.messages.Messages.GherkinDocument.Feature.Tag;
import io.cucumber.plugin.event.TestCase;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
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
        URI testCaseUri = testCase.getUri();
        List<Envelope> sources = Collections.singletonList(
                Gherkin.makeSourceEnvelope(FileUtils.getFileSource(testCaseUri), testCaseUri.toString()));

        List<Envelope> envelopes = Gherkin.fromSources(
                        sources,
                        true,
                        true,
                        true,
                        () -> String.valueOf(UUID.randomUUID()))
                .collect(Collectors.toList());

        GherkinDocument gherkinDocument = envelopes.stream()
                .filter(Messages.Envelope::hasGherkinDocument)
                .map(Messages.Envelope::getGherkinDocument)
                .findFirst()
                .orElse(null);

        return gherkinDocument != null ? gherkinDocument.getFeature() : null;
    }

    private String getScenarioName(final Feature feature, final String testCaseName, final List<String> testCaseTags) {
        List<TableRow> examplesTableRows = getExampleTableRows(feature, testCaseName, testCaseTags);

        int tableRowIndex = IntStream.range(0, examplesTableRows.size())
                .filter(i -> examplesTableRows.get(i).getLocation().getLine() == testCase.getLocation().getLine())
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

        return feature.getChildrenList().stream()
                .map(Feature.FeatureChild::getScenario)
                .filter(scenario -> scenario.getName().equals(testCaseName) &&
                        !scenario.getExamplesList().isEmpty() &&
                        hasTags(scenario, feature.getTagsList(), testCaseTags))
                .flatMap(scenarioOutline -> scenarioOutline.getExamplesList().stream())
                .flatMap(examples -> examples.getTableBodyList().stream())
                .collect(Collectors.toList());
    }

    private boolean hasTags(
            final Scenario scenarioOutline,
            final List<Tag> featureTags,
            final List<String> testCaseTags) {
        List<Tag> scenarioOutlineTags = new ArrayList<>(featureTags);
        scenarioOutlineTags.addAll(scenarioOutline.getTagsList());

        return scenarioOutline.getExamplesList().stream()
                .anyMatch(examples -> {
                    List<Tag> scenarioOutlineAndExamplesTags = new ArrayList<>(scenarioOutlineTags);
                    scenarioOutlineAndExamplesTags.addAll(examples.getTagsList());
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
