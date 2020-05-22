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
            case PASSED:
                testCaseResult.setFinalResultId(FinalResultId.PASSED);
                break;
            case FAILED:
                testCaseResult.setFinalResultId(FinalResultId.FAILED);
                testCaseResult.setFailReason(formatErrorMessage(result.getError()));
                break;
            case PENDING:
                testCaseResult.setFinalResultId(FinalResultId.PENDING);
                testCaseResult.setFailReason(getShortErrorMessage(result.getError()));
                break;
            case SKIPPED:
                testCaseResult.setFinalResultId(FinalResultId.PENDING);
                testCaseResult.setFailReason("Test skipped");
                break;
            default:
                testCaseResult.setFinalResultId(FinalResultId.NOT_EXECUTED);
        }
        return testCaseResult;
    }

    private String formatErrorMessage(final Throwable error) {
        final String message = getShortErrorMessage(error);
        final String stackTrace = ExceptionUtils.getStackTrace(error);
        return format("Message:%n%s%n%nStack Trace:%n%s", message, stackTrace);
    }

    private String getShortErrorMessage(final Throwable error) {
        return error.getMessage().split("\n")[0];
    }

    @Data
    static class TestCaseResult {
        private int finalResultId;
        private String failReason;
    }
}
