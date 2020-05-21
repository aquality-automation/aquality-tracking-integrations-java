package aquality.tracking.integrations.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Test {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("project_id")
    private Integer projectId;
    @JsonProperty("suites")
    private List<Suite> suites;
    @JsonProperty("resolution_colors")
    private String resolutionColors;
    @JsonProperty("result_colors")
    private String resultColors;
    @JsonProperty("result_ids")
    private String resultIds;
}
