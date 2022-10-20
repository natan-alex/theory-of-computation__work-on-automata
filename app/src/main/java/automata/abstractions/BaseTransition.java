package automata.abstractions;

import java.util.Set;

public abstract class BaseTransition {
    public abstract String getSymbol();

    public abstract Set<BaseState> getDestinations();

    public abstract BaseState getOrigin();
}
