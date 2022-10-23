package automata.abstractions;

import java.util.Set;

public abstract class BaseTransition {
    public abstract String getSymbol();

    public abstract Set<BaseState> getDestinations();

    public abstract BaseState getOrigin();

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof BaseTransition)) {
            return false;
        }

        var comparisonObjectAsTransition = (BaseTransition) o;

        var areOriginsEqual = getOrigin().equals(comparisonObjectAsTransition.getOrigin());
        var areSymbolsEqual = getSymbol().compareToIgnoreCase(comparisonObjectAsTransition.getSymbol()) == 0;
        var areDestinationsEqual = getDestinations().equals(comparisonObjectAsTransition.getDestinations());

        return areOriginsEqual && areSymbolsEqual && areDestinationsEqual;
    }
}
