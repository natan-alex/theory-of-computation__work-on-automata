package automata.abstractions;

public interface ITransition {
    String getSymbol();

    IState getDestination();
}
