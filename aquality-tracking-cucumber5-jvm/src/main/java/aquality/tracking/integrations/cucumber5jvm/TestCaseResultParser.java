package aquality.tracking.integrations.cucumber5jvm;

import aquality.tracking.integrations.core.FinalResultId;
import io.cucumber.plugin.event.Result;
import lombok.Data;
import org.apache.commons.lang3.exception.ExceptionUtils;

import static java.lang.String.format;

class TestCaseResultParser {

    private final Result result;

    TestCaseResultParser(Result result) {
        this.result = result;
    }

    TestCaseResult parse() {
        TestCaseResult testCaseResult = new TestCaseResult();
        switch (result.getStatus()) {
            case FAILED:
                testCaseResult.setFinalResultId(FinalResultId.FAILED);
                testCaseResult.setFailReason(formatErrorMessage(result.getError()));
                break;
            case PASSED:
                testCaseResult.setFinalResultId(FinalResultId.PASSED);
                break;
            case PENDING:
            case SKIPPED:
                testCaseResult.setFinalResultId(FinalResultId.PENDING);
                testCaseResult.setFailReason("Test skipped");
                break;
            default:
                testCaseResult.setFinalResultId(FinalResultId.NOT_EXECUTED);
        }
        return testCaseResult;
    }

    private String formatErrorMessage(Throwable error) {
        final String message = error.getMessage().split("\n")[0];
        final String stackTrace = ExceptionUtils.getStackTrace(error);
        return format("Message:%n%s%n%nStack Trace:%n%s", message, stackTrace);
    }

    @Data
    static class TestCaseResult {
        private int finalResultId;
        private String failReason;
    }
}
