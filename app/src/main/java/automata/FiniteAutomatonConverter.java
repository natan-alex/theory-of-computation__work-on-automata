package automata;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import automata.abstractions.BaseState;
import automata.abstractions.BaseTransition;
import automata.abstractions.IFiniteAutomaton;
import automata.abstractions.IFiniteAutomatonConverter;
import automata.abstractions.ITransitionFunction;

public class FiniteAutomatonConverter implements IFiniteAutomatonConverter {
    private Queue<Set<BaseState>> statesToWalkThrought;
    private Set<BaseTransition> newAutomatonTransitions;
    private ITransitionFunction automatonTransitionFunction;
    private IFiniteAutomaton automatonToBeConverted;

    public FiniteAutomatonConverter() {
    }

    private static void validateAutomaton(IFiniteAutomaton automaton) {
        Objects.requireNonNull(automaton);

        if (automaton.isDeterministic()) {
            throw new IllegalArgumentException("The automaton to be converted must be a non deterministic automaton");
        }
    }

    @Override
    public IFiniteAutomaton convertNonDeterministicAutomatonToADeterministicOne(
            IFiniteAutomaton automaton) {
        validateAutomaton(automaton);

        automatonToBeConverted = automaton;
        statesToWalkThrought = new LinkedList<Set<BaseState>>();
        newAutomatonTransitions = new HashSet<BaseTransition>();

        automatonTransitionFunction = automaton.getTransitionFunction();

        statesToWalkThrought.add(Set.of(automaton.getInitialState()));

        while (!statesToWalkThrought.isEmpty()) {
            for (var symbol : automaton.getAlphabet()) {
                var newStateSet = new HashSet<BaseState>();

                System.out.println("symbol: " + symbol);

                for (var state : statesToWalkThrought.peek()) {
                    var destinations = automatonTransitionFunction.whereToGoWith(state, symbol);

                    if (destinations.size() != 0) {
                        newStateSet.addAll(destinations);
                    }

                    System.out.println("state in first: " + state.getIdentifier());
                    System.out.print("destinations: ");
                    destinations.forEach(s -> System.out.print(s.getIdentifier() + ", "));
                    System.out.println("");
                }

                System.out.println("newStates: ");
                statesToWalkThrought.forEach(s -> {
                    System.out.print("\ts: ");
                    s.forEach(i -> System.out.print(i.getIdentifier() + ", "));
                    System.out.println("");
                });

                System.out.print("newStateSet: ");
                newStateSet.forEach(s -> System.out.print(s.getIdentifier() + ", "));
                System.out.println("");

                if (!newStateSet.isEmpty() && !statesToWalkThrought.contains(newStateSet)) {
                    System.out.println("adding newStateSet");
                    statesToWalkThrought.add(newStateSet);
                }

                System.out.println("");

                // TODO: handle new automaton transitions and split code into multiple functions
            }

            statesToWalkThrought.poll();
        }

        return null;
    }
}
