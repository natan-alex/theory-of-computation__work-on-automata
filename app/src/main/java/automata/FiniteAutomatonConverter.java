package automata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
    private List<Set<BaseState>> statesHistory;
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
        automatonTransitionFunction = automaton.getTransitionFunction();

        statesToWalkThrought = new LinkedList<>();
        statesHistory = new ArrayList<>();
        newAutomatonTransitions = new HashSet<>();
        destinationsAndCorrespondingStates = new HashMap<>();

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

            statesHistory.add(statesToWalkThrought.poll());
        }
    }

    private void iterateOverAlphabetAddingNewStatesAndTransitions() {
        var alphabet = automatonToBeConverted.getAlphabet();

        for (var symbol : alphabet) {
            var newStateSet = createStateSetContainingDestinationsForQueueHeadWithSymbol(symbol);

            if (!newStateSet.isEmpty()) {
                if (!statesHistory.contains(newStateSet)) {
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

        var newStateIdentifier = destinationIdentifiers.length > 1
                ? "[" + String.join(", ", destinationIdentifiers) + "]"
                : String.join(", ", destinationIdentifiers);

        var destination = new State(newStateIdentifier);

        if (newStateSet.stream().anyMatch(s -> s.isAFinalState())) {
            destination.setIfIsAFinalState(true);
        }

        var transition = new Transition(origin, symbol, destination);

        destinationsAndCorrespondingStates.put(newStateSet, destination);
        newAutomatonTransitions.add(transition);
    }
}
