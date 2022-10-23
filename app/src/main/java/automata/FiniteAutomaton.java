package automata;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import automata.abstractions.BaseState;
import automata.abstractions.BaseTransition;
import automata.abstractions.IFiniteAutomaton;
import automata.abstractions.ITransitionFunction;
import utils.ArrayUtils;
import utils.CollectionUtils;

public class FiniteAutomaton implements IFiniteAutomaton {
    private final Set<String> alphabet;
    private final Set<BaseState> allStates;
    private final BaseState initialState;
    private final Set<BaseState> finalStates;
    private final ITransitionFunction transitionFunction;
    private final List<BaseState> stepsToCheckIfSentenceWasAcceptable;

    public FiniteAutomaton(Set<? extends BaseTransition> transitionSet) {
        CollectionUtils.throwIfNullOrEmpty(transitionSet, "transitionSet");

        var getInitialStateResult = getInitialStateFrom(transitionSet);

        validateGetInitialStateResult(getInitialStateResult);

        initialState = getInitialStateResult.iterator().next();
        transitionFunction = new TransitionFunction(transitionSet);
        alphabet = getAlphabetFrom(transitionSet);
        allStates = getAllStatesFrom(transitionSet);
        finalStates = getFinalStatesFromAllStates();
        stepsToCheckIfSentenceWasAcceptable = new ArrayList<>();
    }

    private Set<BaseState> getFinalStatesFromAllStates() {
        return allStates.stream()
                .filter(s -> s.isAFinalState())
                .collect(Collectors.toSet());
    }

    private Set<BaseState> getInitialStateFrom(Set<? extends BaseTransition> transitionSet) {
        return transitionSet.stream()
                .map(t -> Stream.concat(t.getDestinations().stream(), Stream.of(t.getOrigin())))
                .flatMap(s -> s)
                .filter(s -> s.isTheInitialState())
                .collect(Collectors.toSet());
    }

    private void validateGetInitialStateResult(Set<? extends BaseState> result) {
        if (result.isEmpty()) {
            throw new IllegalArgumentException("The transition set does not have a initial state defined");
        } else if (result.size() != 1) {
            throw new IllegalArgumentException("The transition set has more than one initial state defined");
        }
    }

    private Set<BaseState> getAllStatesFrom(Set<? extends BaseTransition> transitionSet) {
        return transitionSet.stream()
                .map(t -> Stream.concat(t.getDestinations().stream(), Stream.of(t.getOrigin())))
                .flatMap(s -> s)
                .collect(Collectors.toSet());
    }

    private static Set<String> getAlphabetFrom(Set<? extends BaseTransition> transitions) {
        return transitions.stream()
                .map(t -> t.getSymbol())
                .collect(Collectors.toSet());
    }

    private boolean recursivelyCheckIfIsSentenceAcceptable(String[] sentence) {
        return recursivelyCheckIfIsSentenceAcceptable(sentence, false);
    }

    private boolean recursivelyCheckIfIsSentenceAcceptable(String[] sentence, boolean canComputeSteps) {
        if (canComputeSteps) {
            stepsToCheckIfSentenceWasAcceptable.clear();
        }

        return recursivelyCheckIfIsSentenceAcceptable(initialState, sentence, 0);
    }

    private void registerStepForState(BaseState state) {
        if (stepsToCheckIfSentenceWasAcceptable != null && state != null) {
            stepsToCheckIfSentenceWasAcceptable.add(state);
        }
    }

    private boolean recursivelyCheckIfIsSentenceAcceptable(
            BaseState currentState,
            String[] sentence,
            int currentSymbolIndex) {
        while (currentState != null && currentSymbolIndex < sentence.length) {
            registerStepForState(currentState);

            var currentSymbol = sentence[currentSymbolIndex++];
            var nextStates = transitionFunction.whereToGoWith(currentState, currentSymbol);
            var numberOfNextStates = nextStates.size();

            if (numberOfNextStates == 0) {
                currentState = null;
            } else if (numberOfNextStates == 1) {
                currentState = nextStates.iterator().next();
            } else {
                return handleNonDeterminism(nextStates, sentence, currentSymbolIndex);
            }
        }

        registerStepForState(currentState);

        return currentState != null && currentState.isAFinalState();
    }

    private boolean handleNonDeterminism(
            Set<? extends BaseState> nextStates,
            String[] sentence,
            int currentSymbolIndex) {
        for (var state : nextStates) {
            var wasAccepted = recursivelyCheckIfIsSentenceAcceptable(state, sentence, currentSymbolIndex);

            if (wasAccepted) {
                return true;
            }
        }

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
    public ITransitionFunction getTransitionFunction() {
        return transitionFunction;
    }

    @Override
    public boolean isSentenceAcceptable(String... sentence) {
        ArrayUtils.throwIfNullOrEmpty(sentence, "sentence");
        ArrayUtils.throwIfAnyElementIsNullOrEmpty(sentence, "sentence");

        return recursivelyCheckIfIsSentenceAcceptable(sentence);
    }

    @Override
    public List<BaseState> runStepByStep(String... sentence) {
        ArrayUtils.throwIfNullOrEmpty(sentence, "sentence");
        ArrayUtils.throwIfAnyElementIsNullOrEmpty(sentence, "sentence");

        recursivelyCheckIfIsSentenceAcceptable(sentence, true);

        return stepsToCheckIfSentenceWasAcceptable;
    }

}
