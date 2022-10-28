package automata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import automata.abstractions.BaseState;
import automata.abstractions.BaseTransition;
import automata.abstractions.IFiniteAutomaton;
import automata.abstractions.IFiniteAutomatonConverter;
import automata.abstractions.ITransitionFunction;

public class FiniteAutomatonConverter implements IFiniteAutomatonConverter {
    private IFiniteAutomaton automatonToBeConverted;
    private ITransitionFunction automatonTransitionFunction;
    private Queue<Set<BaseState>> statesToWalkThrought;
    private Map<Set<BaseState>, BaseState> destinationsAndCorrespondingStates;
    private Set<BaseTransition> newAutomatonTransitions;

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
        destinationsAndCorrespondingStates = new HashMap<Set<BaseState>, BaseState>();

        automatonTransitionFunction = automaton.getTransitionFunction();

        initStatesToWalkThroughtAndDestinationsMappings();

        walkThroughtStatesFillingTheTransitionSet();

        return new FiniteAutomaton(newAutomatonTransitions);
    }

    private void initStatesToWalkThroughtAndDestinationsMappings() {
        var initialState = automatonToBeConverted.getInitialState();
        var initialStateSet = Set.of(initialState);

        statesToWalkThrought.add(initialStateSet);
        destinationsAndCorrespondingStates.put(initialStateSet, initialState);
    }

    private void walkThroughtStatesFillingTheTransitionSet() {
        while (!statesToWalkThrought.isEmpty()) {
            iterateOverAlphabetAddingNewStatesAndTransitions();

            statesToWalkThrought.poll();
        }
    }

    private void iterateOverAlphabetAddingNewStatesAndTransitions() {
        var alphabet = automatonToBeConverted.getAlphabet();

        for (var symbol : alphabet) {
            var newStateSet = createStateSetContainingDestinationsForQueueHeadWithSymbol(symbol);

            if (!newStateSet.isEmpty()) {
                if (!statesToWalkThrought.contains(newStateSet)) {
                    statesToWalkThrought.add(newStateSet);
                }

                createAndAddTransitionToNewAutomatonTransitions(symbol, newStateSet);
            }
        }
    }

    private Set<BaseState> createStateSetContainingDestinationsForQueueHeadWithSymbol(
            String symbol) {
        var newStateSet = new HashSet<BaseState>();

        var queueHead = statesToWalkThrought.peek();

        for (var state : queueHead) {
            var destinations = automatonTransitionFunction.whereToGoWith(state, symbol);

            if (destinations.size() != 0) {
                newStateSet.addAll(destinations);
            }
        }

        return newStateSet;
    }

    private void createAndAddTransitionToNewAutomatonTransitions(
            String symbol,
            Set<BaseState> newStateSet) {
        var queueHead = statesToWalkThrought.peek();
        var origin = destinationsAndCorrespondingStates.get(queueHead);

        var destinationIdentifiers = newStateSet.stream()
                .map(s -> s.getIdentifier())
                .toArray(String[]::new);

        var newStateIdentifier = String.join(",", destinationIdentifiers);
        var destination = new State(newStateIdentifier);

        if (newStateSet.stream().anyMatch(s -> s.isAFinalState())) {
            destination.setIfIsAFinalState(true);
        }

        var transition = new Transition(origin, symbol, destination);

        destinationsAndCorrespondingStates.put(newStateSet, destination);
        newAutomatonTransitions.add(transition);
    }
}
