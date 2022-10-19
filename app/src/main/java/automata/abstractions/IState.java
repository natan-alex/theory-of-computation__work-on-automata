package automata.abstractions;

public interface IState {
    String getIdentifier();

    boolean isAFinalState();

    boolean isTheInitialState();
}
