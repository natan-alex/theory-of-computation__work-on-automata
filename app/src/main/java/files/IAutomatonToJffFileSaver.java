package files;

import java.io.IOException;

import automata.abstractions.IFiniteAutomaton;

public interface IAutomatonToJffFileSaver {
    void saveToFile(IFiniteAutomaton automaton, String fileName) throws IOException;
}
