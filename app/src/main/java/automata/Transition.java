package automata;

import java.util.Objects;
import java.util.Set;

import automata.abstractions.BaseState;
import automata.abstractions.BaseTransition;
import utils.StringUtils;

public class Transition extends BaseTransition {
    private final BaseState origin;
    private final String symbol;
    private final Set<BaseState> destinations;

    public Transition(BaseState origin, String symbol, BaseState... destinations) {
        this.origin = Objects.requireNonNull(origin);
        this.symbol = StringUtils.requireNonNullOrEmpty(symbol);
        this.destinations = Set.of(destinations);
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public Set<BaseState> getDestinations() {
        return destinations;
    }

    @Override
    public BaseState getOrigin() {
        return origin;
    }
}
