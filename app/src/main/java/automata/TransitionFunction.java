package automata;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import automata.abstractions.BaseState;
import automata.abstractions.BaseTransition;
import automata.abstractions.ITransitionFunction;
import utils.CollectionUtils;
import utils.StringUtils;

public class TransitionFunction implements ITransitionFunction {
    private final Map<BaseState, Map<String, Set<BaseState>>> transitions;

    public TransitionFunction(Set<? extends BaseTransition> transitionSet) {
        CollectionUtils.throwIfNullOrEmpty(transitionSet, "transitionSet");

        this.transitions = new HashMap<>();

        for (var transition : transitionSet) {
            transitions.putIfAbsent(transition.getOrigin(), new HashMap<>());
            var originMappings = transitions.get(transition.getOrigin());
            originMappings.putIfAbsent(transition.getSymbol(), new HashSet<>());
            var symbolMappings = originMappings.get(transition.getSymbol());
            symbolMappings.addAll(transition.getDestinations());
        }
    }

    @Override
    public Set<BaseState> whereToGoWith(BaseState origin, String symbol) {
        Objects.requireNonNull(origin);
        StringUtils.throwIfNullOrEmpty(symbol, "symbol");

        var possibleSymbols = transitions.get(origin);

        if (possibleSymbols == null || possibleSymbols.isEmpty()) {
            return Collections.emptySet();
        }

        var destinationStates = possibleSymbols.get(symbol);

        if (destinationStates == null || destinationStates.isEmpty()) {
            return Collections.emptySet();
        }

        return destinationStates;
    }
}
