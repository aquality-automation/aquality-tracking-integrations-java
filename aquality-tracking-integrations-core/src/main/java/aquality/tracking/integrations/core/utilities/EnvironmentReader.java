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

        T resultValue;
        if (tClass.equals(Boolean.class)) {
            resultValue = (T) Boolean.valueOf(envVar);
        } else if (tClass.equals(Integer.class)) {
            resultValue = (T) Integer.valueOf(envVar);
        } else {
            resultValue = (T) envVar;
        }
        return resultValue;
    }
}
