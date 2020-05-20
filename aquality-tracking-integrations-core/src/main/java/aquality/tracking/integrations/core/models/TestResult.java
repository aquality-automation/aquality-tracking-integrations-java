package aquality.tracking.integrations.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TestResult {
    @JsonProperty("project_id")
    private Integer projectId;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("test_id")
    private Integer testId;
    @JsonProperty("final_result_id")
    private Integer finalResultId;
    @JsonProperty("test_run_id")
    private Integer testRunId;
    @JsonProperty("debug")
    private Integer debug;
    @JsonProperty("updated")
    private String updated;
    @JsonProperty("start_date")
    private String startDate;
    @JsonProperty("finish_date")
    private String finishDate;
    @JsonProperty("final_result_updated")
    private String finishResultUpdated;
    @JsonProperty("pending")
    private Integer pending;
    @JsonProperty("fail_reason")
    private String failReason;
}
