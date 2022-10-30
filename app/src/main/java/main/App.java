package main;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Scanner;
import java.util.stream.Collectors;

import automata.FiniteAutomatonConverter;
import automata.abstractions.BaseState;
import automata.abstractions.IFiniteAutomaton;
import automata.abstractions.IFiniteAutomatonConverter;
import files.AutomatonToJffFileSaver;
import files.IAutomatonToJffFileSaver;
import files.IJffFileParser;
import files.JffFileParser;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final IJffFileParser fileParser = new JffFileParser();
    private static final IFiniteAutomatonConverter converter = new FiniteAutomatonConverter();
    private static final IAutomatonToJffFileSaver saver = new AutomatonToJffFileSaver();
    private static int option;
    private static IFiniteAutomaton loadedAutomaton;
    private static IFiniteAutomaton convertedAutomaton;

    public static void main(String[] args) {
        do {
            System.out.println("\n---------- MENU ----------\n");
            System.out.println("[0] - Exit");
            System.out.println("[1] - Load automaton from a jff file");
            System.out.println("[2] - Simulate loaded automaton");
            System.out.println("[3] - Convert loaded automaton to a deterministic one");
            System.out.println("[4] - Save converted automaton to a file");
            System.out.println("");
            readOptionUntilValid();
            handleOptionTyped();
        } while (option != 0);
    }

    private static void readOptionUntilValid() {
        var typedAValidOption = false;

        while (!typedAValidOption) {
            System.out.print("Enter one option: ");

            option = scanner.nextInt();

            if (option < 0 || option > 4) {
                System.out.println("Invalid option. Please type again.");
            } else {
                typedAValidOption = true;
            }
        }
    }

    private static void handleOptionTyped() {
        switch (option) {
            case 1:
                loadAutomatonFromFileOptionTyped();
                break;
            case 2:
                simulateAutomatonOptionTyped();
                break;
            case 3:
                convertAutomatonOptionTyped();
                break;
            case 4:
                saveConvertedAutomatonToAFile();
                break;
            default:
                break;
        }
    }

    private static String clearBufferThenReadLine() {
        if (scanner.hasNext()) {
            scanner.nextLine();
        }

        return scanner.nextLine();
    }

    private static String getJoinedStatesIdentifiers(Collection<BaseState> states) {
        return String.join(", ", states
                .stream()
                .map(s -> s.getIdentifier())
                .collect(Collectors.toList()));
    }

    private static void printAutomatonInfos(IFiniteAutomaton automaton) {
        var alphabet = String.join(", ", automaton.getAlphabet());
        var allStates = getJoinedStatesIdentifiers(automaton.getAllStates());
        var initialState = automaton.getInitialState().getIdentifier();
        var finalStates = getJoinedStatesIdentifiers(automaton.getFinalStates());
        System.out.println("Automaton information: ");
        System.out.println("\tIs deterministic? " + automaton.isDeterministic());
        System.out.println("\tAlphabet: " + alphabet);
        System.out.println("\tStates: " + allStates);
        System.out.println("\tInitial state: " + initialState);
        System.out.println("\tFinal states: " + finalStates);
        System.out.println("\tTransitions: ");
        printAutomatonTransitions(automaton);
    }

    private static void printAutomatonTransitions(IFiniteAutomaton automaton) {
        var transitionFunction = automaton.getTransitionFunction();

        for (var state : automaton.getAllStates()) {
            for (var symbol : automaton.getAlphabet()) {
                var whereToGo = transitionFunction.whereToGoWith(state, symbol);

                if (!whereToGo.isEmpty()) {
                    var joinedStates = getJoinedStatesIdentifiers(whereToGo);

                    System.out.println(
                            "\t\tFrom " + state.getIdentifier() + " with " + symbol + ": goes to " + joinedStates);
                }
            }
        }
    }

    private static void loadAutomatonFromFileOptionTyped() {
        try {
            System.out.print("File path: ");

            var input = clearBufferThenReadLine();
            var path = Path.of(input);

            loadedAutomaton = fileParser.parseFile(path);

            System.out.println("Automaton successfully loaded.");

            printAutomatonInfos(loadedAutomaton);
        } catch (Exception e) {
            System.out.println("Failed to read file. Reason: " + e.getMessage());
        }
    }

    private static void convertAutomatonOptionTyped() {
        if (loadedAutomaton == null) {
            System.out.println("There is no automaton to be converted. Load one before.");
        }

        if (loadedAutomaton.isDeterministic()) {
            System.out.println(
                    "The conversion is just from a non deterministic automaton to a deterministic one. The loaded automaton is already deterministic.");
        }

        try {
            convertedAutomaton = converter.convertNonDeterministicAutomatonToADeterministicOne(loadedAutomaton);

            System.out.println("Automaton succesfully converted.");

            printAutomatonInfos(convertedAutomaton);
        } catch (Exception e) {
            System.out.println("Failed to convert automaton. Reason: " + e.getMessage());
        }
    }

    private static void simulateAutomatonOptionTyped() {
        try {
            System.out.print("Sentence: (separate symbols with a space): ");
            var sentence = clearBufferThenReadLine();
            var symbols = sentence.split(" ");
            var result = loadedAutomaton.simulate(symbols);
            System.out.println("Is sentence acceptable? " + result.wasSentenceAccepted());
            System.out.println("Visited states: " + getJoinedStatesIdentifiers(result.getVisitedStates()));
        } catch (Exception e) {
            System.out.println("Failed to execute simulation. Reason: " + e.getMessage());
        }
    }

    private static void saveConvertedAutomatonToAFile() {
        if (convertedAutomaton == null) {
            System.out.println("There is no converted automaton to be saved. Convert an automaton before.");
        }

        System.out.print("File name: ");
        var fileName = clearBufferThenReadLine();

        try {
            saver.saveToFile(convertedAutomaton, fileName);
            System.out.println("Automaton saved succesfully.");
        } catch (Exception e) {
            System.out.println("Failed to save automaton to file. Reason: " + e.getMessage());
        }
    }
}
