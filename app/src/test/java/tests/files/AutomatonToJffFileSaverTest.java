package tests.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import automata.FiniteAutomaton;
import automata.State;
import automata.Transition;
import automata.abstractions.BaseState;
import automata.abstractions.IFiniteAutomaton;

import files.AutomatonToJffFileSaver;
import files.IAutomatonToJffFileSaver;

public class AutomatonToJffFileSaverTest {
    private IAutomatonToJffFileSaver saver;
    private IFiniteAutomaton automatonToSave;

    @Before
    public void setup() {
        saver = new AutomatonToJffFileSaver();

        var states = new BaseState[] { new State("0"), new State("1") };

        states[0].setIfIsTheInitialState(true);

        states[1].setIfIsAFinalState(true);

        var transitions = Set.of(
                new Transition(states[0], "a", states[0], states[1]),
                new Transition(states[1], "b", states[1]));

        automatonToSave = new FiniteAutomaton(transitions);
    }

    @Test
    public void throwsIfAutomatonOrFileNameAreInvalid() {
        assertThrows(NullPointerException.class, () -> {
            saver.saveToFile(null, "hey");
        });

        assertThrows(NullPointerException.class, () -> {
            saver.saveToFile(automatonToSave, null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            saver.saveToFile(automatonToSave, "");
        });
    }

    @Test
    public void throwsIfFileNameDoesNotContainJffExtension() {
        assertThrows(IllegalArgumentException.class, () -> {
            saver.saveToFile(automatonToSave, "bla");
        });
    }

    @Test
    public void correctlyAddStatesToFile() throws IOException {
        var fileName = "testing-conversion.jff";
        saver.saveToFile(automatonToSave, fileName);

        assertTrue(Files.exists(Path.of(fileName)));

        var fileContent = Files.readAllLines(Path.of(fileName));

        for (var state : automatonToSave.getAllStates()) {
            assertTrue(fileContent.stream()
                    .anyMatch(line -> line.contains("<state id=\"" + state.getIdentifier() + "\">")));
        }
    }

    @Test
    public void correctlyAddTransitionsToFile() throws IOException {
        var fileName = "testing-conversion.jff";
        saver.saveToFile(automatonToSave, fileName);

        assertTrue(Files.exists(Path.of(fileName)));

        var fileContent = Files.readAllLines(Path.of(fileName));

        for (var state : automatonToSave.getAllStates()) {
            for (var symbol : automatonToSave.getAlphabet()) {
                var whereToGo = automatonToSave.getTransitionFunction().whereToGoWith(state, symbol);

                assertTrue(fileContent.stream()
                        .anyMatch(line -> line.contains("<from>" + state.getIdentifier() + "</from>")));

                for (var destination : whereToGo) {
                    assertTrue(fileContent.stream()
                            .anyMatch(line -> line.contains("<to>" + destination.getIdentifier() + "</to>")));
                }

                assertTrue(fileContent.stream()
                        .anyMatch(line -> line.contains("<read>" + symbol + "</read>")));
            }
        }
    }
}
