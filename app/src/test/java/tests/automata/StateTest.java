package tests.automata;

import org.junit.Test;
import static org.junit.Assert.*;

import automata.State;

public class StateTest {
    @Test
    public void twoStatesAreEqualIfTheyHaveTheSameIdentifier() {
        var state1 = new State("a");
        var state2 = new State("A");

        assertTrue(state1.equals(state2));
    }

    @Test
    public void equalityFailsIfComparisonObjectIsNotOfTypeOrIsNull() {
        var state1 = new State("a");
        State state2 = null;
        var state3 = "a";

        assertFalse(state1.equals(state2));
        assertFalse(state1.equals(state3));
    }

    @Test
    public void aStateMustHaveAnIdentifierOnItsCreation() {
        assertThrows(NullPointerException.class, () -> {
            new State(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new State("");
        });
    }
}
