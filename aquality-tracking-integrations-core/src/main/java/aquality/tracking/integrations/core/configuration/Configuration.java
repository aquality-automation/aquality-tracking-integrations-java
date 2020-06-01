package aquality.tracking.integrations.core.configuration;

import lombok.Setter;

import static aquality.tracking.integrations.core.utilities.EnvironmentReader.getValueOrDefault;

@Setter
public class Configuration implements IConfiguration {
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
    private String attachmentsDirectory;

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

    public String getAttachmentsDirectory() {
        return getValueOrDefault("aquality.attachmentsDirectory", String.class, attachmentsDirectory);
    }
}
