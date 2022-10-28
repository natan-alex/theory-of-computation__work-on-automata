package files;

import java.io.FileNotFoundException;

import automata.abstractions.IFiniteAutomaton;
import utils.FileUtils;
import utils.StringUtils;

public class JffFileReader implements IJffFileReader {

    @Override
    public IFiniteAutomaton parseFile(String filePath) throws FileNotFoundException {
        StringUtils.throwIfNullOrEmpty(filePath, "filePath");
        FileUtils.throwIfFileDoesNotExistAt(filePath);
        return null;
    }
}
