package tests.automata;

import java.util.List;
import java.util.Set;

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
        someStates = new BaseState[] {
                new State("0"),
                new State("1"),
                new State("2"),
                new State("3"),
                new State("4")
        };

        someStates[0].setIfIsTheInitialState(true);

        someStates[3].setIfIsAFinalState(true);
        someStates[4].setIfIsAFinalState(true);

        someTransitions = Set.of(
                new Transition(someStates[0], "a", someStates[1]),
                new Transition(someStates[1], "b", someStates[3]),
                new Transition(someStates[2], "c", someStates[2], someStates[4]),
                new Transition(someStates[2], "d", someStates[0]),
                new Transition(someStates[3], "b", someStates[2], someStates[4]),
                new Transition(someStates[4], "a", someStates[1], someStates[3]));

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
            automaton.simulate(sentence);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            automaton.simulate(new String[0]);
        });
    }

    @Test
    public void throwsIfTheSentenceContainsAnyElementThatIsNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            automaton.simulate(new String[] { "" });
        });

        assertThrows(IllegalArgumentException.class, () -> {
            automaton.simulate(new String[] { null });
        });
    }

    @Test
    public void rejectSentencesWithSymbolsThatAreNotPresentInTheTransitionSet() {
        var result = automaton.simulate("x", "y");
        assertFalse(result.wasSentenceAccepted());
    }

    @Test
    public void acceptValidSentenceThatJustRequireADeterministicFiniteAutomaton() {
        var result = automaton.simulate("a", "b");
        assertTrue(result.wasSentenceAccepted());
    }

    @Test
    public void acceptValidSentenceThatRequireANonDeterministicFiniteAutomaton() {
        var result = automaton.simulate("a", "b", "b");
        assertTrue(result.wasSentenceAccepted());

        result = automaton.simulate("a", "b", "b", "c");
        assertTrue(result.wasSentenceAccepted());
    }

    @Test
    public void rejectInvalidSentenceThatRequireANonDeterministicFiniteAutomaton() {
        var result = automaton.simulate("a", "b", "b", "d");
        assertFalse(result.wasSentenceAccepted());
    }

    @Test
    public void stepsAreCorrectlyReturnedForDeterministicSimpleCase() {
        var result = List.of(someStates[0], someStates[1], someStates[3]);
        var givenResult = automaton.simulate("a", "b").getVisitedStates();

        assertEquals(result, givenResult);
    }

    @Test
    public void stepsAreCorrectlyReturnedWhenABranchOccursAndAllStatesAreChecked() {
        var possibleResults = List.of(
                List.of(someStates[0], someStates[1], someStates[3], someStates[2], someStates[4]),
                List.of(someStates[0], someStates[1], someStates[3], someStates[4]));

        var givenResult = automaton.simulate("a", "b", "b").getVisitedStates();

        assertTrue(possibleResults.stream().anyMatch(r -> r.equals(givenResult)));
    }

    /*
     * someTransitions.add(new Transition(someStates[0], "a", someStates[1]));
     * someTransitions.add(new Transition(someStates[1], "b", someStates[3]));
     * someTransitions.add(new Transition(someStates[2], "c", someStates[2],
     * someStates[4]));
     * someTransitions.add(new Transition(someStates[2], "d", someStates[0]));
     * someTransitions.add(new Transition(someStates[3], "b", someStates[2],
     * someStates[4]));
     * someTransitions.add(new Transition(someStates[4], "a", someStates[1],
     * someStates[3]));
     */

    @Test
    public void stepsAreCorrectlyReturnedWhenABranchOccursAndAStateIsAccepted() {
        var possibleResults = List.of(
                List.of(someStates[0], someStates[1], someStates[3], someStates[2], someStates[2],
                        someStates[4]),
                List.of(someStates[0], someStates[1], someStates[3], someStates[2], someStates[4]),
                List.of(someStates[0], someStates[1], someStates[3], someStates[4], someStates[2],
                        someStates[4]),
                List.of(someStates[0], someStates[1], someStates[3], someStates[4], someStates[2],
                        someStates[2], someStates[4]));
        var givenResult = automaton.simulate("a", "b", "b", "c").getVisitedStates();
        assertTrue(possibleResults.stream().anyMatch(r -> r.equals(givenResult)));
    }
}
