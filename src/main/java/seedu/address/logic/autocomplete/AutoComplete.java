package seedu.address.logic.autocomplete;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.logic.commands.AddAppointmentToPetCommand;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddPetCommand;
import seedu.address.logic.commands.AddVetTechToAppointmentCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeletePetCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.RemoveAppointmentFromPetCommand;
import seedu.address.logic.commands.RemoveVetTechFromAppointmentCommand;
import seedu.address.logic.commands.ScheduleCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.SortAppointmentCommand;
import seedu.address.logic.commands.SortClientCommand;
import seedu.address.logic.commands.SortPetCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.Prefix;

/**
 * the main AutoCompleteManager of the application
 */
public class AutoComplete {

    private static final String LIST_CLIENT_PREFIX = " client";
    private static final String LIST_VETTECH_PREFIX = " vettech";
    private static final String LIST_PET_PREFIX = " pet";
    private Trie commandTrie;
    private CommandParameterSyntaxHandler commandParameterSyntaxHandler;

    public AutoComplete() {
        commandTrie = new Trie();
        commandParameterSyntaxHandler = new CommandParameterSyntaxHandler();
        initCommandKeyWords();
    }

    /**
     * Initialises command keywords in commandTrie
     */
    private void initCommandKeyWords() {
        commandTrie.insertWord(AddCommand.COMMAND_WORD);
        commandTrie.insertWord(AddPetCommand.COMMAND_WORD);
        commandTrie.insertWord(ClearCommand.COMMAND_WORD);
        commandTrie.insertWord(DeleteCommand.COMMAND_WORD);
        commandTrie.insertWord(DeletePetCommand.COMMAND_WORD);
        commandTrie.insertWord(EditCommand.COMMAND_WORD);
        commandTrie.insertWord(ExitCommand.COMMAND_WORD);
        commandTrie.insertWord(FindCommand.COMMAND_WORD);
        commandTrie.insertWord(HelpCommand.COMMAND_WORD);
        commandTrie.insertWord(HistoryCommand.COMMAND_WORD);
        commandTrie.insertWord(RedoCommand.COMMAND_WORD);
        commandTrie.insertWord(ScheduleCommand.COMMAND_WORD);
        commandTrie.insertWord(SelectCommand.COMMAND_WORD);
        commandTrie.insertWord(UndoCommand.COMMAND_WORD);

        commandTrie.insertWord(AddAppointmentToPetCommand.COMMAND_WORD);
        commandTrie.insertWord(RemoveAppointmentFromPetCommand.COMMAND_WORD);
        commandTrie.insertWord(AddVetTechToAppointmentCommand.COMMAND_WORD);
        commandTrie.insertWord(RemoveVetTechFromAppointmentCommand.COMMAND_WORD);

        commandTrie.insertWord(SortClientCommand.COMMAND_WORD);
        commandTrie.insertWord(SortAppointmentCommand.COMMAND_WORD);
        commandTrie.insertWord(SortPetCommand.COMMAND_WORD);

        commandTrie.insertWord(ListCommand.COMMAND_WORD);
        commandTrie.insertWord(ListCommand.COMMAND_WORD + LIST_CLIENT_PREFIX);
        commandTrie.insertWord(ListCommand.COMMAND_WORD + LIST_VETTECH_PREFIX);
        commandTrie.insertWord(ListCommand.COMMAND_WORD + LIST_PET_PREFIX);


    }

    /**
     * Returns a sorted list of auto completed commands with prefix {@code keyWord}
     *
     */
    public List<String> autoCompleteCommands(String keyWord) {
        requireNonNull(keyWord);
        return commandTrie.autoComplete(keyWord).stream()
                .sorted(Comparator.comparingInt(String::length))
                .collect(Collectors.toList());
    }

    /**
     * Returns the concatenation String of the next missing prefix parameter with the input string.
     */
    public String autoCompleteNextMissingParameter(String input) {
        requireNonNull(input);
        if (input.isEmpty()) {
            return input;
        }
        String command = input.split(" ")[0];

        ArrayList<Prefix> missingPrefixes = commandParameterSyntaxHandler.getMissingPrefixes(command, input);
        String completedText = input;

        if (!missingPrefixes.isEmpty()) {
            completedText = completedText + missingPrefixes.get(0);
        }

        return completedText;
    }
}
