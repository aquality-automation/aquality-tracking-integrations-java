package aquality.tracking.integrations.core.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.UncheckedIOException;

import static java.lang.String.format;

public class JsonMapper {

    private JsonMapper(){
    }

    public static  <T> T mapFileContent(final String filename, Class<T> tClass) {
        String fileContent = FileReader.readResourceFile(filename);
        return mapStringContent(fileContent, tClass);
    }

    public static <T> T mapStringContent(final String content, TypeReference<T> typeReference) {
        try {
            return new ObjectMapper().readValue(content, typeReference);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(format("Exception occurred during mapping %n%s%n to %s",
                    content, typeReference.getType()), e);
        }
    }

    public static <T> T mapStringContent(final String content, Class<T> tClass) {
        try {
            return new ObjectMapper().readValue(content, tClass);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(format("Exception occurred during mapping %n%s%n to %s",
                    content, tClass.getName()), e);
        }
    }

    public static String getJson(Object data) {
        try {
            return new ObjectMapper().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(format("Exception occurred during converting %s to JSON", data), e);
        }
    }
}
