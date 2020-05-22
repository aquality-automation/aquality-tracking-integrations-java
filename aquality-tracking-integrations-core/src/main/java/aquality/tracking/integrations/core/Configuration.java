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
        return getEnvVarOrDefault("aquality.isEnabled", Boolean.class, enabled);
    }

    public String getSuiteName() {
        return getEnvVarOrDefault("aquality.suiteName", String.class, suiteName);
    }

    public String getBuildName() {
        return getEnvVarOrDefault("aquality.buildName", String.class, buildName);
    }

    public String getEnvironment() {
        return getEnvVarOrDefault("aquality.environment", String.class, environment);
    }

    public String getCiBuild() {
        return getEnvVarOrDefault("aquality.ciBuild", String.class, ciBuild);
    }

    public boolean isDebug() {
        return getEnvVarOrDefault("aquality.debug", Boolean.class, debug);
    }

    @SuppressWarnings("unchecked")
    private <T> T getEnvVarOrDefault(final String varName, Class<T> tClass, T defaultValue) {
        final String envVar = System.getProperty(varName);
        if (envVar == null) {
            return defaultValue;
        }

        T resultValue = null;
        if (tClass.equals(Boolean.class)) {
            resultValue = (T) Boolean.valueOf(envVar);
        }
        return resultValue;
    }
}
