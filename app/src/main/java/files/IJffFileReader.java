package files;

import java.io.FileNotFoundException;

import automata.abstractions.IFiniteAutomaton;

public interface IJffFileReader {
    IFiniteAutomaton parseFile(String filePath) throws FileNotFoundException;
}
