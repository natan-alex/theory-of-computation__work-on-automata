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
import java.util.List;

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
    public void convertedAutomatonHasThingsThatAreSimilarToTheOriginalOne() {
        var converted = converter.convertNonDeterministicAutomatonToADeterministicOne(automatonToBeConverted);

        assertTrue(converted.isDeterministic());
        assertEquals(converted.getAlphabet(), automatonToBeConverted.getAlphabet());
        assertEquals(converted.getInitialState(), automatonToBeConverted.getInitialState());
    }

    @Test
    public void convertedAutomatonHasTheCorrectStates() {
        var converted = converter.convertNonDeterministicAutomatonToADeterministicOne(automatonToBeConverted);

        var states = converted.getAllStates();

        var statesThatMustBePresent = List.of(
                new State("0"),
                new State("1"));
        var oneOfTheseMustBePresent = List.of(
                new State("1,0"),
                new State("0,1"));

        assertTrue(states.containsAll(statesThatMustBePresent));
        assertTrue(oneOfTheseMustBePresent.stream().anyMatch(s -> states.contains(s)));
    }

    @Test
    public void convertedAutomatonHasTheCorrectFinalStates() {
        var converted = converter.convertNonDeterministicAutomatonToADeterministicOne(automatonToBeConverted);

        var states = converted.getFinalStates();

        var statesThatMustBePresent = List.of(
                new State("1"));
        var oneOfTheseMustBePresent = List.of(
                new State("1,0"),
                new State("0,1"));

        assertTrue(states.containsAll(statesThatMustBePresent));
        assertTrue(oneOfTheseMustBePresent.stream().anyMatch(s -> states.contains(s)));
    }

    @Test
    public void convertedAutomatonHasTheCorrectTransitions() {
        var converted = converter.convertNonDeterministicAutomatonToADeterministicOne(automatonToBeConverted);

        var transitionFunction = converted.getTransitionFunction();

        var state0 = new State("0");
        var state1 = new State("1");
        var oneOfTheseIsPresent = List.of(new State("1,0"), new State("0,1"));

        assertTrue(oneOfTheseIsPresent.stream()
                .anyMatch(s -> transitionFunction.whereToGoWith(state0, "a").equals(Set.of(s))));
        assertTrue(
                oneOfTheseIsPresent.stream().anyMatch(s -> transitionFunction.whereToGoWith(s, "a").equals(Set.of(s))));
        assertTrue(
                oneOfTheseIsPresent.stream()
                        .anyMatch(s -> transitionFunction.whereToGoWith(s, "b").equals(Set.of(state1))));
        assertEquals(Set.of(state1), transitionFunction.whereToGoWith(state1, "b"));
    }
}
