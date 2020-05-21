package aquality.tracking.integrations.core.utilities;

import aquality.tracking.integrations.core.AqualityUncheckedException;

import java.io.*;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileReader {

    private FileReader() {
    }

    public static String readResourceFile(final String filename) {
        try (InputStream inputStream = Objects.requireNonNull(FileReader.class.getClassLoader().getResourceAsStream(filename));
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader br = new BufferedReader(inputStreamReader)) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            throw new AqualityUncheckedException(String.format("Reading of resource file '%1$s' was failed", filename), e);
        }
    }
}
