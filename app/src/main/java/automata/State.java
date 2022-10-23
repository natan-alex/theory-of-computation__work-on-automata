package automata;

import automata.abstractions.BaseState;
import utils.StringUtils;

public class State extends BaseState {
    private final String identifier;
    private boolean isAFinalState;
    private boolean isTheInitialState;

    public State(String identifier) {
        StringUtils.throwIfNullOrEmpty(identifier, "identifier");
        this.identifier = identifier;
        this.isAFinalState = false;
        this.isTheInitialState = false;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean isAFinalState() {
        return isAFinalState;
    }

    @Override
    public boolean isTheInitialState() {
        return isTheInitialState;
    }

    @Override
    public void setIfIsAFinalState(boolean isAFinalState) {
        this.isAFinalState = isAFinalState;
    }

    @Override
    public void setIfIsTheInitialState(boolean isTheInitialState) {
        this.isTheInitialState = isTheInitialState;
    }
}
