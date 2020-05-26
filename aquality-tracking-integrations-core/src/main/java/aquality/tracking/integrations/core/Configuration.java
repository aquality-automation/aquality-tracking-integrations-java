package aquality.tracking.integrations.core;

import lombok.Data;

import static aquality.tracking.integrations.core.utilities.EnvironmentReader.getValueOrDefault;

@Data
public class Configuration {
    private boolean enabled;
    private String host;
    private String token;
    private int projectId;
    private String executor;
    private String suiteName;
    private String buildName;
    private String environment;
    private String ciBuild;
    private boolean debug;

    public boolean isEnabled() {
        return getValueOrDefault("aquality.enabled", Boolean.class, enabled);
    }

    public String getHost() {
        return getValueOrDefault("aquality.host", String.class, host);
    }

    public String getToken() {
        return getValueOrDefault("aquality.token", String.class, token);
    }

    public int getProjectId() {
        return getValueOrDefault("aquality.projectId", Integer.class, projectId);
    }

    public String getExecutor() {
        return getValueOrDefault("aquality.executor", String.class, executor);
    }

    public String getSuiteName() {
        return getValueOrDefault("aquality.suiteName", String.class, suiteName);
    }

    public String getBuildName() {
        return getValueOrDefault("aquality.buildName", String.class, buildName);
    }

    public String getEnvironment() {
        return getValueOrDefault("aquality.environment", String.class, environment);
    }

    public String getCiBuild() {
        return getValueOrDefault("aquality.ciBuild", String.class, ciBuild);
    }

    public boolean isDebug() {
        return getValueOrDefault("aquality.debug", Boolean.class, debug);
    }
}
