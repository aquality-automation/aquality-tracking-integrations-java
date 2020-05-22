package aquality.tracking.integrations.core;

import lombok.Data;

@Data
public class Configuration {
    private String host;
    private String token;
    private int projectId;
    private String executor;

    private boolean enabled;
    private String suiteName;
    private String buildName;
    private String environment;
    private String ciBuild;
    private int debug = 0;

    public boolean isEnabled() {
        String envVar = getEnvVar("isAqualityTrackingEnabled");
        return envVar != null ? Boolean.parseBoolean(envVar) : enabled;
    }

    public String getSuiteName() {
        String envVar = getEnvVar("suiteName");
        return envVar != null ? envVar : suiteName;
    }

    public String getBuildName() {
        String envVar = getEnvVar("buildName");
        return envVar != null ? envVar : buildName;
    }

    public String getEnvironment() {
        String envVar = getEnvVar("environment");
        return envVar != null ? envVar : environment;
    }

    public String getCiBuild() {
        String envVar = getEnvVar("ciBuild");
        return envVar != null ? envVar : ciBuild;
    }

    public int getDebug() {
        String envVar = getEnvVar("debug");
        return envVar != null ? Integer.parseInt(envVar) : debug;
    }

    private String getEnvVar(final String varName) {
        return System.getProperty(varName);
    }
}
