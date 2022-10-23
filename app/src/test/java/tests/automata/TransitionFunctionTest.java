package tests.automata;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import automata.State;
import automata.Transition;
import automata.TransitionFunction;
import automata.abstractions.BaseState;
import automata.abstractions.BaseTransition;
import automata.abstractions.ITransitionFunction;

public class TransitionFunctionTest {
    private static final BaseState[] someStates = new BaseState[] {
            new State("0"),
            new State("1"),
            new State("2"),
            new State("3"),
            new State("4")
    };

    private Set<BaseTransition> someTransitions;
    private ITransitionFunction transitionFunction;

    @Before
    public void setup() {
        someStates[0].setIfIsTheInitialState(true);

        someStates[3].setIfIsAFinalState(true);
        someStates[4].setIfIsAFinalState(true);

        someTransitions = new LinkedHashSet<>();

        someTransitions.add(new Transition(someStates[0], "a", someStates[1]));
        someTransitions.add(new Transition(someStates[1], "b", someStates[3]));
        someTransitions.add(new Transition(someStates[2], "c", someStates[2], someStates[4]));
        someTransitions.add(new Transition(someStates[2], "d", someStates[0]));
        someTransitions.add(new Transition(someStates[3], "b", someStates[2], someStates[4]));
        someTransitions.add(new Transition(someStates[4], "a", someStates[1], someStates[3]));

        transitionFunction = new TransitionFunction(someTransitions);
    }

    @Test
    public void throwsIfTheTransitionSetIsNullOrEmpty() {
        assertThrows(NullPointerException.class, () -> {
            new TransitionFunction(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new TransitionFunction(Set.of());
        });
    }

    @Test
    public void throwsIfTheOriginIsNull() {
        assertThrows(NullPointerException.class, () -> {
            transitionFunction.whereToGoWith(null, "");
        });
    }

    @Test
    public void throwsIfTheSymbolIsNullOrEmpty() {
        assertThrows(NullPointerException.class, () -> {
            transitionFunction.whereToGoWith(someStates[0], null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            transitionFunction.whereToGoWith(someStates[0], "");
        });
    }

    @Test
    public void correctlyReturnTheDestinationSet() {
        assertEquals(Set.of(someStates[1]),
                transitionFunction.whereToGoWith(someStates[0], "a"));

        assertEquals(Set.of(someStates[2], someStates[4]),
                transitionFunction.whereToGoWith(someStates[2], "c"));
    }

    @Test
    public void correctlyGiveAnEmptySetAsTheDestination() {
        assertEquals(Set.of(), transitionFunction.whereToGoWith(someStates[0], "b"));
    }
}
