package aquality.tracking.integrations.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TestRun {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("build_name")
    private String buildName;
    @JsonProperty("start_time")
    private String startTime;
    @JsonProperty("test_suite_id")
    private Integer testSuiteId;
    @JsonProperty("project_id")
    private Integer projectId;
    @JsonProperty("finish_time")
    private String finishTime;
    @JsonProperty("debug")
    private Integer debug;
    @JsonProperty("label_id")
    private Integer labelId;
    @JsonProperty("ci_build")
    private String ciBuild;
    @JsonProperty("execution_environment")
    private String executionEnvironment;
    @JsonProperty("milestone_id")
    private Integer milestoneId;
}
