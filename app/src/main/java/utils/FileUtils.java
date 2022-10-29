package utils;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class FileUtils {
    public static boolean existsFileAt(Path path) {
        Objects.requireNonNull(path);
        return Files.exists(path);
    }

    public static void throwIfFileDoesNotExistAt(Path path) throws FileNotFoundException {
        if (!existsFileAt(path)) {
            throw new FileNotFoundException("The file was not found at path " + path);
        }
    }
}
