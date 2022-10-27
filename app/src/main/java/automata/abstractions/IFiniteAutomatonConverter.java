package automata.abstractions;

public interface IFiniteAutomatonConverter {
    IFiniteAutomaton convertNonDeterministicAutomatonToADeterministicOne(IFiniteAutomaton automaton);
}
