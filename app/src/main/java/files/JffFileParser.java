package files;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import automata.FiniteAutomaton;
import automata.State;
import automata.Transition;
import automata.abstractions.BaseState;
import automata.abstractions.BaseTransition;
import automata.abstractions.IFiniteAutomaton;
import utils.FileUtils;

public class JffFileParser implements IJffFileParser {
    private boolean aTagIsOpen;
    private List<String> tagRelatedLines;
    private BaseState lastAddedState;
    private Set<BaseState> states;
    private Set<BaseTransition> transitions;

    @Override
    public IFiniteAutomaton parseFile(Path path) throws FileNotFoundException {
        FileUtils.throwIfFileDoesNotExistAt(path);

        try {
            var iterator = Files.lines(path).iterator();

            walkThroughtFileLinesExtractingInformation(iterator);
        } catch (IOException e) {
            System.out.println("e: " + e);
        }

        return new FiniteAutomaton(transitions);
    }

    private void walkThroughtFileLinesExtractingInformation(Iterator<String> lines) {
        aTagIsOpen = false;
        tagRelatedLines = new ArrayList<>();
        states = new HashSet<>();
        transitions = new HashSet<>();

        while (lines.hasNext()) {
            handleFileLine(lines.next());
        }
    }

    private void handleFileLine(String line) {
        if (line.contains("<state")) {
            handleOpeningStateTagOnLine(line);
            lineContainsAnOpenTag();
        } else if (line.contains("</state")) {
            handleClosingStateTagOnLine(line);
            lineContainsACloseTag();
        } else if (line.contains("<transition")) {
            lineContainsAnOpenTag();
        } else if (line.contains("</transition")) {
            handleClosingTransitionTag();
            lineContainsACloseTag();
        } else if (aTagIsOpen) {
            tagRelatedLines.add(line);
        }
    }

    private void lineContainsAnOpenTag() {
        aTagIsOpen = true;
    }

    private void lineContainsACloseTag() {
        if (aTagIsOpen) {
            aTagIsOpen = false;
            tagRelatedLines.clear();
        }
    }

    private String getTagContent(String line) {
        var endOfLeftSide = line.indexOf(">");
        var startOfRightSide = line.indexOf("<", endOfLeftSide);
        return line.substring(endOfLeftSide + 1, startOfRightSide).trim();
    }

    private Optional<BaseState> getStateByIdentifier(String identifier) {
        return states.stream()
                .filter(s -> s.getIdentifier().compareToIgnoreCase(identifier) == 0)
                .findFirst();
    }

    private void handleOpeningStateTagOnLine(String line) {
        var firstDoubleQuoteIndex = line.indexOf("\"");
        var lastDoubleQuoteIndex = line.indexOf("\"", firstDoubleQuoteIndex + 1);
        var id = line.substring(firstDoubleQuoteIndex + 1, lastDoubleQuoteIndex);

        lastAddedState = new State(id);
        states.add(lastAddedState);
    }

    private void handleClosingStateTagOnLine(String line) {
        if (tagRelatedLines.stream().anyMatch(l -> l.contains("initial"))) {
            lastAddedState.setIfIsTheInitialState(true);
        } else if (tagRelatedLines.stream().anyMatch(l -> l.contains("final"))) {
            lastAddedState.setIfIsAFinalState(true);
        }
    }

    private void handleClosingTransitionTag() {
        String symbol = null;
        BaseState origin = null, destination = null;

        for (var line : tagRelatedLines) {
            var tagContent = getTagContent(line);

            if (line.contains("from")) {
                origin = getStateByIdentifier(tagContent).orElseThrow();
            } else if (line.contains("to")) {
                destination = getStateByIdentifier(tagContent).orElseThrow();
            } else if (line.contains("read")) {
                symbol = tagContent;
            }
        }

        var newTransition = new Transition(origin, symbol, destination);
        transitions.add(newTransition);
    }
}
