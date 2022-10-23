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
    private Set<BaseTransition> someTransitions;
    private IFiniteAutomaton automaton;

    @Before
    public void setup() {
        someStates = IntStream.rangeClosed(1, 5)
                .boxed()
                .map(i -> new State(i.toString()))
                .toArray(State[]::new);

        someTransitions = Set.of(
                new Transition(someStates[0], "a", someStates[1]),
                new Transition(someStates[1], "b", someStates[3]),
                new Transition(someStates[2], "c", someStates[2], someStates[4]),
                new Transition(someStates[2], "d", someStates[0]),
                new Transition(someStates[3], "b", someStates[2], someStates[4]),
                new Transition(someStates[4], "a", someStates[1], someStates[3]));

        someStates[0].setIfIsTheInitialState(true);

        someStates[3].setIfIsAFinalState(true);
        someStates[4].setIfIsAFinalState(true);

        automaton = new FiniteAutomaton(someTransitions);
    }

    @Test
    public void throwsIfTheTransitionSetIsNullOrEmpty() {
        assertThrows(NullPointerException.class, () -> {
            new FiniteAutomaton(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new FiniteAutomaton(Set.of());
        });
    }

    @Test
    public void alphabetContainsAllSymbolsPresentInTheTransitions() {
        assertEquals(Set.of("a", "b", "c", "d"), automaton.getAlphabet());
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
            new FiniteAutomaton(someTransitions);
        });
    }

    @Test
    public void throwsIfThereAreMoreThanOneInitialState() {
        someStates[1].setIfIsTheInitialState(true);

        assertThrows(IllegalArgumentException.class, () -> {
            new FiniteAutomaton(someTransitions);
        });
    }

    @Test
    public void succesfullyGetTheFinalStates() {
        assertEquals(Set.of(someStates[3], someStates[4]), automaton.getFinalStates());
    }

    @Test
    public void throwsIfTheSentenceIsNullOrEmpty() {
        assertThrows(NullPointerException.class, () -> {
            String[] sentence = null;
            automaton.isSentenceAcceptable(sentence);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            automaton.isSentenceAcceptable(new String[0]);
        });
    }

    @Test
    public void throwsIfTheSentenceContainsAnyElementThatIsNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            automaton.isSentenceAcceptable(new String[] { "" });
        });

        assertThrows(IllegalArgumentException.class, () -> {
            automaton.isSentenceAcceptable(new String[] { null });
        });
    }

    @Test
    public void rejectSentencesWithSymbolsThatAreNotPresentInTheTransitionSet() {
        assertFalse(automaton.isSentenceAcceptable("x", "y"));
    }

    @Test
    public void acceptValidSentenceThatJustRequireADeterministicFiniteAutomaton() {
        assertTrue(automaton.isSentenceAcceptable("a", "b"));
    }

    @Test
    public void acceptValidSentenceThatRequireANonDeterministicFiniteAutomaton() {
        assertTrue(automaton.isSentenceAcceptable("a", "b", "b"));
        assertTrue(automaton.isSentenceAcceptable("a", "b", "b", "c"));
    }

    @Test
    public void rejectInvalidSentenceThatRequireANonDeterministicFiniteAutomaton() {
        assertFalse(automaton.isSentenceAcceptable("a", "b", "b", "d"));
    }
}
