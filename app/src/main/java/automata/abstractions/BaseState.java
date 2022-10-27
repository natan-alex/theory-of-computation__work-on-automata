package automata.abstractions;

public abstract class BaseState {
    public abstract String getIdentifier();

    public abstract boolean isAFinalState();

    public abstract boolean isTheInitialState();

    public abstract void setIfIsAFinalState(boolean isAFinalState);

    public abstract void setIfIsTheInitialState(boolean isTheInitialState);

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof BaseState)) {
            return false;
        }

        var identifier = this.getIdentifier();
        var otherObjectIdentifier = ((BaseState) o).getIdentifier();

        return identifier.compareToIgnoreCase(otherObjectIdentifier) == 0;
    }

    @Override
    public int hashCode() {
        return getIdentifier().hashCode();
    }
}
