package automata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

        alphabet = new HashSet<>();
        allStates = new HashSet<>();
        finalStates = new HashSet<>();
        stepsToCheckIfSentenceWasAcceptable = new ArrayList<>();
        transitionFunction = new TransitionFunction(transitionSet);

        for (var transition : transitionSet) {
            alphabet.add(transition.getSymbol());
            allStates.add(transition.getOrigin());
            allStates.addAll(transition.getDestinations());
            addToFinalStateSet(transition);
        }

        initialState = getAndValidateInitialState();
    }

    private void addToFinalStateSet(BaseTransition transition) {
        var origin = transition.getOrigin();
        var destinations = transition.getDestinations();

        if (origin.isAFinalState()) {
            finalStates.add(origin);
        }

        destinations.stream()
                .filter(s -> s.isAFinalState())
                .forEach(s -> finalStates.add(s));
    }

    private BaseState getAndValidateInitialState() {
        var initialStates = allStates.stream()
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
        if (state != null) {
            stepsToCheckIfSentenceWasAcceptable.add(state);
        }
    }

    private boolean recursivelyCheckIfIsSentenceAcceptable(
            BaseState currentState,
            String[] sentence,
            int currentSymbolIndex) {
        System.out.println("current state: " + currentState != null ? currentState.getIdentifier() : "null");
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
