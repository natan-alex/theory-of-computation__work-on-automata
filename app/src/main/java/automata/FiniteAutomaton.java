package automata;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import utils.ArrayUtils;
import utils.CollectionUtils;
import automata.abstractions.AutomatonSimulationResult;
import automata.abstractions.BaseState;
import automata.abstractions.BaseTransition;
import automata.abstractions.IFiniteAutomaton;
import automata.abstractions.ITransitionFunction;

public class FiniteAutomaton implements IFiniteAutomaton {
    private final Set<String> alphabet;
    private final Set<BaseState> allStates;
    private final BaseState initialState;
    private final Set<BaseState> finalStates;
    private final ITransitionFunction transitionFunction;
    private final boolean isDeterministic;

    public FiniteAutomaton(Set<? extends BaseTransition> transitionSet) {
        CollectionUtils.throwIfNullOrEmpty(transitionSet, "transitionSet");

        initialState = extractTheInitialStateAndValidateIt(transitionSet);
        allStates = extractAllStatesFrom(transitionSet);
        alphabet = extractAlphabetFrom(transitionSet);
        finalStates = extractFinalStatesFrom(transitionSet);
        transitionFunction = new TransitionFunction(transitionSet);
        isDeterministic = checkIfAutomatonIsDeterministic();
    }

    private BaseState extractTheInitialStateAndValidateIt(Set<? extends BaseTransition> transitionSet) {
        var initialStates = transitionSet.stream()
                .map(t -> Stream.concat(Stream.of(t.getOrigin()), t.getDestinations().stream()))
                .flatMap(s -> s)
                .filter(s -> s.isTheInitialState())
                .collect(Collectors.toSet());

        if (initialStates.isEmpty()) {
            throw new IllegalArgumentException("The transition set does not have a initial state defined");
        }

        if (initialStates.size() != 1) {
            throw new IllegalArgumentException("The transition set has more than one initial state defined");
        }

        return initialStates.iterator().next();
    }

    private Set<String> extractAlphabetFrom(Set<? extends BaseTransition> transitionSet) {
        return transitionSet.stream().map(s -> s.getSymbol()).collect(Collectors.toSet());
    }

    private Set<BaseState> extractAllStatesFrom(Set<? extends BaseTransition> transitionSet) {
        return transitionSet.stream()
                .map(t -> Stream.concat(Stream.of(t.getOrigin()), t.getDestinations().stream()))
                .flatMap(s -> s)
                .collect(Collectors.toSet());
    }

    private Set<BaseState> extractFinalStatesFrom(Set<? extends BaseTransition> transitionSet) {
        return transitionSet.stream()
                .map(t -> Stream.concat(Stream.of(t.getOrigin()), t.getDestinations().stream()))
                .flatMap(s -> s)
                .filter(s -> s.isAFinalState())
                .collect(Collectors.toSet());
    }

    private boolean checkIfAutomatonIsDeterministic() {
        for (var state : allStates) {
            for (var symbol : alphabet) {
                var whereToGo = transitionFunction.whereToGoWith(state, symbol);

                if (!whereToGo.isEmpty() && whereToGo.size() > 1) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isSentenceAcceptable(
            String[] sentence,
            BaseState currentState,
            int currentSymbolIndex,
            List<BaseState> visitedStates) {
        visitedStates.add(currentState);

        if (currentSymbolIndex == sentence.length) {
            return currentState.isAFinalState();
        }

        var currentSymbol = sentence[currentSymbolIndex];
        var nextStates = transitionFunction.whereToGoWith(currentState, currentSymbol);

        // non determinism: create a machine for each state (if there is more than
        // one possible state to go to) and check if this machine can accept the
        // sentence. Keep doing this while the sentence was not accepted
        for (var state : nextStates) {
            if (isSentenceAcceptable(sentence, state, currentSymbolIndex + 1, visitedStates)) {
                return true;
            }
        }

        // if none of the next states was accepted or there
        // is no state to go to, then the sentence cannot be accepted
        return false;
    }

    @Override
    public Set<String> getAlphabet() {
        return alphabet;
    }

    @Override
    public Set<BaseState> getAllStates() {
        return allStates;
    }

    @Override
    public BaseState getInitialState() {
        return initialState;
    }

    @Override
    public Set<BaseState> getFinalStates() {
        return finalStates;
    }

    @Override
    public boolean isDeterministic() {
        return isDeterministic;
    }

    @Override
    public ITransitionFunction getTransitionFunction() {
        return transitionFunction;
    }

    @Override
    public AutomatonSimulationResult simulate(String... sentence) {
        ArrayUtils.throwIfNullOrEmpty(sentence, "sentence");
        ArrayUtils.throwIfAnyElementIsNullOrEmpty(sentence, "sentence");

        var visitedStates = new ArrayList<BaseState>();
        var isAcceptable = isSentenceAcceptable(sentence, initialState, 0, visitedStates);
        return new AutomatonSimulationResult(isAcceptable, visitedStates);
    }
}
