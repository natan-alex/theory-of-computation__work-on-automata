package automata.abstractions;

import java.util.List;

import utils.CollectionUtils;

public class AutomatonSimulationResult {
    private final boolean wasSentenceAccepted;
    private final List<BaseState> visitedStates;

    public AutomatonSimulationResult(boolean wasSentenceAcceptable, List<BaseState> visitedStates) {
        CollectionUtils.throwIfNullOrEmpty(visitedStates, "steps");

        this.wasSentenceAccepted = wasSentenceAcceptable;
        this.visitedStates = visitedStates;
    }

    public boolean wasSentenceAccepted() {
        return wasSentenceAccepted;
    }

    public List<BaseState> getVisitedStates() {
        return visitedStates;
    }
}
