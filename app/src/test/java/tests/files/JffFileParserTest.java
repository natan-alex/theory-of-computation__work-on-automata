package tests.files;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import automata.State;
import automata.abstractions.BaseState;

import files.IJffFileParser;
import files.JffFileParser;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Set;

public class JffFileParserTest {
    private IJffFileParser parser;

    @Before
    public void setup() {
        parser = new JffFileParser();
    }

    @Test
    public void throwsIfTheFilePathIsInvalid() {
        assertThrows(NullPointerException.class, () -> {
            parser.parseFile(null);
        });
    }

    @Test
    public void throwsIfTheFileDoesNotExistAtTheProvidedPath() {
        assertThrows(FileNotFoundException.class, () -> {
            parser.parseFile(Path.of("bla.jff"));
        });
    }

    @Test
    public void generatedAutomatonHasTheCorrectBasicThings() throws FileNotFoundException {
        var currentDir = System.getProperty("user.dir");
        var jffFilePath = Path.of(currentDir, "src", "main", "resources", "testFile.jff");
        var automaton = parser.parseFile(jffFilePath);

        assertEquals(automaton.getAlphabet(), Set.of("a", "b"));
        assertEquals(automaton.getInitialState(), new State("0"));
        assertEquals(automaton.getFinalStates(), Set.of(new State("1")));
        assertTrue(automaton.isDeterministic());
    }

    @Test
    public void generatedAutomatonHasTheCorrectTransitionFunction() throws FileNotFoundException {
        var currentDir = System.getProperty("user.dir");
        var jffFilePath = Path.of(currentDir, "src", "main", "resources", "testFile.jff");
        var automaton = parser.parseFile(jffFilePath);
        var transitionFunction = automaton.getTransitionFunction();

        var states = new BaseState[] {
                new State("0"),
                new State("1"),
                new State("2"),
        };

        assertEquals(Set.of(states[1]), transitionFunction.whereToGoWith(states[0], "b"));
        assertEquals(Set.of(states[0]), transitionFunction.whereToGoWith(states[0], "a"));
        assertEquals(Set.of(states[2]), transitionFunction.whereToGoWith(states[1], "b"));
        assertEquals(Set.of(states[1]), transitionFunction.whereToGoWith(states[2], "b"));
    }
}
