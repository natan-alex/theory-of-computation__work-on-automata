package tests.automata;

import org.junit.Test;
import static org.junit.Assert.*;

import automata.State;
import automata.Transition;
import automata.abstractions.BaseState;

public class TransitionTest {
    private static final BaseState[] someStates = new BaseState[] {
            new State("foo"),
            new State("bar"),
    };

    @Test
    public void itMustHaveAValidOriginAValidSymbolAndValidDestinationsOnCreation() {
        assertThrows(NullPointerException.class, () -> {
            new Transition(null, "a", someStates[1]);
        });

        assertThrows(NullPointerException.class, () -> {
            new Transition(someStates[0], null, someStates[1]);
        });

        assertThrows(NullPointerException.class, () -> {
            BaseState destination = null;
            new Transition(someStates[0], "a", destination);
        });

        assertThrows(NullPointerException.class, () -> {
            BaseState[] destinations = null;
            new Transition(someStates[0], "a", destinations);
        });
    }

    @Test
    public void successfullyDetectDuplicatedStateInDestinations() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Transition(someStates[0], "a", someStates[0], someStates[0], someStates[1]);
        });
    }

    @Test
    public void equalityFailsIfComparisonObjectIsNotOfTypeOrIsNull() {
        var transition1 = new Transition(someStates[0], "a", someStates[1]);
        Transition transition2 = null;
        var transition3 = someStates[0];

        assertFalse(transition1.equals(transition2));
        assertFalse(transition1.equals(transition3));
    }

    @Test
    public void equalitySucceedsIfTransitionsAreEqual() {
        var transition1 = new Transition(someStates[0], "a", someStates[1]);
        var transition2 = new Transition(someStates[0], "A", someStates[1]);
        var transition3 = new Transition(someStates[0], "b", someStates[0], someStates[1]);
        var transition4 = new Transition(someStates[0], "B", someStates[1], someStates[0]);

        assertTrue(transition1.equals(transition2));
        assertTrue(transition3.equals(transition4));
    }
}
