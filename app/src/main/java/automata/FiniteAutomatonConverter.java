package automata;

import java.util.Objects;

import automata.abstractions.IFiniteAutomaton;
import automata.abstractions.IFiniteAutomatonConverter;

public class FiniteAutomatonConverter implements IFiniteAutomatonConverter {
    public FiniteAutomatonConverter() {
    }

    @Override
    public IFiniteAutomaton convertNonDeterministicAutomatonToADeterministicOne(IFiniteAutomaton automaton) {
        Objects.requireNonNull(automaton);

        return null;
    }
}
