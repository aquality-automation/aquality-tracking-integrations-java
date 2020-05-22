package aquality.tracking.integrations.core.utilities;

import aquality.tracking.integrations.core.AqualityUncheckedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class FileReader {

    private FileReader() {
    }

    public static String readResourceFile(final String filename) {
        try (InputStream inputStream = FileReader.class.getClassLoader().getResourceAsStream(filename)) {
            if (inputStream == null) {
                throw new AqualityUncheckedException(format("Resource %s not found. Please add it to the resources folder.", filename));
            }

            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 BufferedReader br = new BufferedReader(inputStreamReader)) {
                return br.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        } catch (IOException e) {
            throw new AqualityUncheckedException(format("Reading of resource file %s was failed", filename), e);
        }
    }
}
