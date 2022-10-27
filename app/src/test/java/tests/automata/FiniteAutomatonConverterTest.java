package tests.automata;

import automata.abstractions.IFiniteAutomaton;
import automata.abstractions.IFiniteAutomatonConverter;
import automata.FiniteAutomaton;
import automata.FiniteAutomatonConverter;
import automata.State;
import automata.Transition;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Set;

public class FiniteAutomatonConverterTest {
    private IFiniteAutomatonConverter converter;
    private IFiniteAutomaton automatonToBeConverted;

    @Before
    public void setup() {
        converter = new FiniteAutomatonConverter();

        var states = new State[] { new State("0"), new State("1") };

        states[0].setIfIsTheInitialState(true);

        states[1].setIfIsAFinalState(true);

        var transitions = Set.of(
                new Transition(states[0], "a", states[0], states[1]),
                new Transition(states[1], "b", states[1]));

        automatonToBeConverted = new FiniteAutomaton(transitions);
    }

    @Test
    public void throwsIfTheAutomatonIsNull() {
        assertThrows(NullPointerException.class, () -> {
            converter.convertNonDeterministicAutomatonToADeterministicOne(null);
        });
    }

    @Test
    public void cannotAcceptADeterministicAutomatonToConvert() {
        var states = new State[] { new State("0"), new State("1") };

        states[0].setIfIsTheInitialState(true);

        states[1].setIfIsAFinalState(true);

        var transitions = Set.of(
                new Transition(states[0], "a", states[1]),
                new Transition(states[1], "b", states[1]));

        var automaton = new FiniteAutomaton(transitions);

        assertThrows(IllegalArgumentException.class, () -> {
            converter.convertNonDeterministicAutomatonToADeterministicOne(automaton);
        });
    }

    @Test
    public void convertedAutomatonHasManyThingsThatAreTheSameToTheOriginalOne() {
        var converted = converter.convertNonDeterministicAutomatonToADeterministicOne(automatonToBeConverted);

        assertTrue(converted.isDeterministic());
        assertEquals(converted.getAlphabet(), automatonToBeConverted.getAlphabet());
        assertEquals(converted.getInitialState(), automatonToBeConverted.getInitialState());
    }
}
