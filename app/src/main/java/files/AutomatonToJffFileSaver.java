package files;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

import automata.abstractions.IFiniteAutomaton;
import utils.FileUtils;
import utils.StringUtils;

public class AutomatonToJffFileSaver implements IAutomatonToJffFileSaver {
    private IFiniteAutomaton automatonToSave;
    private String saveAtFileName;
    private BufferedWriter fileWriter;

    @Override
    public void saveToFile(IFiniteAutomaton automaton, String fileName) throws IOException {
        Objects.requireNonNull(automaton);
        StringUtils.throwIfNullOrEmpty(fileName, "fileName");
        FileUtils.throwIfFileNameDoesNotHaveSpecificExtension(fileName, ".jff");

        automatonToSave = automaton;
        saveAtFileName = fileName;

        createFileAndSetWriter();
        writeHeadersAndBasicTagsToFile();
        writeAutomatonStatesToFile();
        writeAutomatonTransitionsToFile();
        closeBasicTagsAtTheEnd();
        flushContentToFileAndCloseWriter();
    }

    private void createFileAndSetWriter() throws IOException {
        fileWriter = new BufferedWriter(new FileWriter(saveAtFileName));
    }

    private void writeToFile(String content, boolean addNewLineAfter) throws IOException {
        fileWriter.append(content);
        fileWriter.newLine();
    }

    private void writeToFileThenAddNewLine(String content) throws IOException {
        writeToFile(content, true);
    }

    private void writeHeadersAndBasicTagsToFile() throws IOException {
        writeToFileThenAddNewLine("<structure>");
        writeToFileThenAddNewLine("\t<type>fa</type>");
    }

    private void writeAutomatonStatesToFile() throws IOException {
        for (var state : automatonToSave.getAllStates()) {
            writeToFileThenAddNewLine("\t<state id=\"" + state.getIdentifier() + "\">");
            writeToFileThenAddNewLine("\t\t<x>0.00</x>");
            writeToFileThenAddNewLine("\t\t<y>0.00</y>");

            if (state.isTheInitialState()) {
                writeToFileThenAddNewLine("\t\t<initial/>");
            }

            if (state.isAFinalState()) {
                writeToFileThenAddNewLine("\t\t<final/>");
            }

            writeToFileThenAddNewLine("\t</state>");
        }
    }

    private void writeAutomatonTransitionsToFile() throws IOException {
        var transitionFunction = automatonToSave.getTransitionFunction();

        for (var state : automatonToSave.getAllStates()) {
            for (var symbol : automatonToSave.getAlphabet()) {
                var whereToGo = transitionFunction.whereToGoWith(state, symbol);

                for (var destination : whereToGo) {
                    writeToFileThenAddNewLine("\t<transition>");
                    writeToFileThenAddNewLine("\t\t<from>" + state.getIdentifier() + "</from>");
                    writeToFileThenAddNewLine("\t\t<to>" + destination.getIdentifier() + "</to>");
                    writeToFileThenAddNewLine("\t\t<read>" + symbol + "</read>");
                    writeToFileThenAddNewLine("\t</transition>");
                }
            }
        }
    }

    private void closeBasicTagsAtTheEnd() throws IOException {
        writeToFile("</structure>", false);
    }

    private void flushContentToFileAndCloseWriter() throws IOException {
        fileWriter.flush();
        fileWriter.close();
    }
}
