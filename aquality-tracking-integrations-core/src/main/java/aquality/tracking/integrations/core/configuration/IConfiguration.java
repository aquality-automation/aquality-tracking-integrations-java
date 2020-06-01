package aquality.tracking.integrations.core.configuration;

public interface IConfiguration {
    boolean isEnabled();

    String getHost();

    String getToken();

    int getProjectId();

    String getExecutor();

    String getSuiteName();

    String getBuildName();

    String getEnvironment();

    String getCiBuild();

    boolean isDebug();

    String getAttachmentsDirectory();
}
