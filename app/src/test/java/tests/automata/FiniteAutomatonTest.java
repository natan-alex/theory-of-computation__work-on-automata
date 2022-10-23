package tests.automata;

import java.util.Set;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import automata.FiniteAutomaton;
import automata.State;
import automata.Transition;
import automata.abstractions.BaseState;
import automata.abstractions.BaseTransition;
import automata.abstractions.IFiniteAutomaton;

public class FiniteAutomatonTest {
    private BaseState[] someStates;
    private BaseTransition[] someTransitions;
    private IFiniteAutomaton automaton;

    @Before
    public void setup() {
        someStates = IntStream.rangeClosed(1, 5)
                .boxed()
                .map(i -> new State(i.toString()))
                .toArray(State[]::new);

        someTransitions = new Transition[] {
                new Transition(someStates[0], "a", someStates[1]),
                new Transition(someStates[1], "b", someStates[3]),
                new Transition(someStates[2], "c", someStates[2], someStates[4]),
                new Transition(someStates[4], "a", someStates[1], someStates[3]),
        };

        someStates[0].setIfIsTheInitialState(true);

        someStates[3].setIfIsAFinalState(true);
        someStates[4].setIfIsAFinalState(true);

        automaton = new FiniteAutomaton(Set.of(someTransitions));
    }

    @Test
    public void cannotAcceptNullOrEmptyTransitions() {
        assertThrows(NullPointerException.class, () -> {
            new FiniteAutomaton(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new FiniteAutomaton(Set.of());
        });
    }

    @Test
    public void alphabetContainsAllSymbolsPresentInTheTransitions() {
        assertEquals(Set.of("a", "b", "c"), automaton.getAlphabet());
    }

    @Test
    public void stateSetContainsAllStatePresentInTheTransitions() {
        assertEquals(Set.of(someStates), automaton.getAllStates());
    }

    @Test
    public void initialStateIsTheCorrectOne() {
        assertEquals(someStates[0], automaton.getInitialState());
    }

    @Test
    public void throwsIfNoneStateIsTheInitialState() {
        someStates[0].setIfIsTheInitialState(false);

        assertThrows(IllegalArgumentException.class, () -> {
            new FiniteAutomaton(Set.of(someTransitions));
        });
    }

    @Test
    public void throwsIfThereAreMoreThanOneInitialState() {
        someStates[1].setIfIsTheInitialState(true);

        assertThrows(IllegalArgumentException.class, () -> {
            new FiniteAutomaton(Set.of(someTransitions));
        });
    }

    @Test
    public void succesfullyGetTheFinalStates() {
        assertEquals(Set.of(someStates[3], someStates[4]), automaton.getFinalStates());
    }
}
