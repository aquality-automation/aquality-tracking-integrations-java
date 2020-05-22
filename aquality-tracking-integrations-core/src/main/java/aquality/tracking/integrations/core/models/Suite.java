package aquality.tracking.integrations.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Suite {
    private Integer id;
    private String name;
    @JsonProperty("project_id")
    private Integer projectId;
}
