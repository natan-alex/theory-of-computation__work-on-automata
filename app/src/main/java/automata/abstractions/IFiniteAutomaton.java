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

public interface IFiniteAutomaton {
    String[] getAlphabet();

    IState[] getAllStates();

    IState getInitialState();

    IState[] getFinalStates();

    Map<IState, ITransition[]> getTransitions();
}
