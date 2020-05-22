package aquality.tracking.integrations.core.utilities;

public class EnvironmentReader {

    private EnvironmentReader() {
    }

    @SuppressWarnings("unchecked")
    public static  <T> T getValueOrDefault(final String varName, Class<T> tClass, T defaultValue) {
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
