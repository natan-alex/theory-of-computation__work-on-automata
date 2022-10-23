package tests.automata;

import java.util.Set;
import java.util.stream.IntStream;

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
    private BaseState[] someStates;
    private Set<BaseTransition> someTransitions;
    private ITransitionFunction transitionFunction;

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
                new Transition(someStates[4], "a", someStates[1], someStates[3]));

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
