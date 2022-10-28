package utils;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {
    public static boolean existsFileAt(String path) {
        StringUtils.throwIfNullOrEmpty(path, "path");
        return Files.exists(Path.of(path));
    }

    public static void throwIfFileDoesNotExistAt(String path) throws FileNotFoundException {
        if (!existsFileAt(path)) {
            throw new FileNotFoundException("The file was not found at path " + path);
        }
    }
}
