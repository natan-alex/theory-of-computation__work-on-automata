package automata;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import automata.abstractions.BaseState;
import automata.abstractions.BaseTransition;
import automata.abstractions.IFiniteAutomaton;
import automata.abstractions.ITransitionFunction;
import utils.CollectionsUtils;

public class FiniteAutomaton implements IFiniteAutomaton {
    private final Set<String> alphabet;
    private final Set<BaseState> allStates;
    private final BaseState initialState;
    private final Set<BaseState> finalStates;
    private final ITransitionFunction transitionFunction;

    public FiniteAutomaton(Set<BaseTransition> transitionSet) {
        CollectionsUtils.throwIfNullOrEmpty(transitionSet);

        var getInitialStateResult = getInitialStateFrom(transitionSet);

        validateGetInitialStateResult(getInitialStateResult);

        initialState = getInitialStateResult.iterator().next();
        transitionFunction = new TransitionFunction(transitionSet);
        alphabet = getAlphabetFrom(transitionSet);
        allStates = getAllStatesFrom(transitionSet);
        finalStates = getFinalStatesFromAllStates();
    }

    private Set<BaseState> getFinalStatesFromAllStates() {
        return allStates.stream()
                .filter(s -> s.isAFinalState())
                .collect(Collectors.toSet());
    }

    private Set<BaseState> getInitialStateFrom(Set<BaseTransition> transitionSet) {
        return transitionSet.stream()
                .map(t -> Stream.concat(t.getDestinations().stream(), Stream.of(t.getOrigin())))
                .flatMap(s -> s)
                .filter(s -> s.isTheInitialState())
                .collect(Collectors.toSet());
    }

    private void validateGetInitialStateResult(Set<BaseState> result) {
        if (result.isEmpty()) {
            throw new IllegalArgumentException("The transition set does not have a initial state defined");
        } else if (result.size() != 1) {
            throw new IllegalArgumentException("The transition set has more than one initial state defined");
        }
    }

    private Set<BaseState> getAllStatesFrom(Set<BaseTransition> transitionSet) {
        return transitionSet.stream()
                .map(t -> Stream.concat(t.getDestinations().stream(), Stream.of(t.getOrigin())))
                .flatMap(s -> s)
                .collect(Collectors.toSet());
    }

    private static Set<String> getAlphabetFrom(Set<BaseTransition> transitions) {
        return transitions.stream()
                .map(t -> t.getSymbol())
                .collect(Collectors.toSet());
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
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public BaseState[] runStepByStep(String... sentence) {
        // TODO Auto-generated method stub
        return null;
    }
}
