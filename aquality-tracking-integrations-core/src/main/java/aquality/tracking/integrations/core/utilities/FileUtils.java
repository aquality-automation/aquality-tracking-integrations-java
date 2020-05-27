package aquality.tracking.integrations.core.utilities;

import aquality.tracking.integrations.core.AqualityUncheckedException;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

public class FileUtils {

    private FileUtils() {
    }

    public static String readResourceFile(final String filename) {
        try (InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream(filename)) {
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

    public static String getFileSource(final URI uri) {
        try (Stream<String> lines = Files.lines(Paths.get(uri))) {
            return lines.collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            throw new AqualityUncheckedException(format("File %s not found.", uri), e);
        }
    }

    public static File writeToFile(final String pathname, final byte[] data) {
        File file = new File(pathname);
        try {
            org.apache.commons.io.FileUtils.writeByteArrayToFile(file, data);
        } catch (IOException e) {
            throw new AqualityUncheckedException(format("Failed to write data into %s", pathname), e);
        }
        return file;
    }
}
