package automata.abstractions;

import java.util.Set;

public interface ITransitionFunction {
    Set<BaseState> whereToGoWith(BaseState origin, String symbol);
}
