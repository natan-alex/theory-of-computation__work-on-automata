/*
 * An automaton is composed by:
 * Alphabet -> a collection of symbols
 * State set
 * Transition function
 * Initial state
 * Final states
 */

package automata.abstractions;

import java.util.Map;
import java.util.Set;

public interface IFiniteAutomaton {
    Set<String> getAlphabet();

    Set<BaseState> getAllStates();

    BaseState getInitialState();

    Set<BaseState> getFinalStates();

    Map<BaseState, BaseTransition[]> getTransitions();
}
