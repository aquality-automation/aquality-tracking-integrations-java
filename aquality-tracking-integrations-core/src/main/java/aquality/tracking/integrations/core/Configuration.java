package aquality.tracking.integrations.core;

import aquality.tracking.integrations.core.utilities.JsonMapper;
import lombok.Data;

@Data
public class Configuration {

    private String host;
    private String token;
    private int projectId;

    private static Configuration instance;

    private Configuration() {
    }

    public static Configuration getInstance() {
        if (instance == null) {
            instance = JsonMapper.mapFileContent("aqualityTracking.json", Configuration.class);
        }
        return instance;
    }
}
