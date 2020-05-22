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
    private boolean debug;

    public boolean isEnabled() {
        String envVar = getEnvVar("aquality.isEnabled");
        return envVar != null ? Boolean.parseBoolean(envVar) : enabled;
    }

    public String getSuiteName() {
        String envVar = getEnvVar("aquality.suiteName");
        return envVar != null ? envVar : suiteName;
    }

    public String getBuildName() {
        String envVar = getEnvVar("aquality.buildName");
        return envVar != null ? envVar : buildName;
    }

    public String getEnvironment() {
        String envVar = getEnvVar("aquality.environment");
        return envVar != null ? envVar : environment;
    }

    public String getCiBuild() {
        String envVar = getEnvVar("aquality.ciBuild");
        return envVar != null ? envVar : ciBuild;
    }

    public boolean isDebug() {
        String envVar = getEnvVar("aquality.debug");
        return envVar != null ? Boolean.parseBoolean(envVar) : debug;
    }

    private String getEnvVar(final String varName) {
        return System.getProperty(varName);
    }
}
