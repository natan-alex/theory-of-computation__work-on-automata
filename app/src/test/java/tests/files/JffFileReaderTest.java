package tests.files;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import files.IJffFileReader;
import files.JffFileReader;

import java.io.FileNotFoundException;

public class JffFileReaderTest {
    private IJffFileReader fileReader;

    @Before
    public void setup() {
        fileReader = new JffFileReader();
    }

    @Test
    public void throwsIfTheFilePathIsInvalid() {
        assertThrows(NullPointerException.class, () -> {
            fileReader.parseFile(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            fileReader.parseFile("");
        });
    }

    @Test
    public void throwsIfTheFileDoesNotExistAtTheProvidedPath() {
        assertThrows(FileNotFoundException.class, () -> {
            fileReader.parseFile("bla.jff");
        });
    }
}
