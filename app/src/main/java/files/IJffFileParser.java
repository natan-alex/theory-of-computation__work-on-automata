package files;

import java.io.FileNotFoundException;
import java.nio.file.Path;

import automata.abstractions.IFiniteAutomaton;

public interface IJffFileParser {
    IFiniteAutomaton parseFile(Path path) throws FileNotFoundException;
}
