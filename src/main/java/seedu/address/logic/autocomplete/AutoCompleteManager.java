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
import seedu.address.logic.commands.ListAllCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.RemoveAppointmentFromPetCommand;
import seedu.address.logic.commands.RemoveVetTechFromAppointmentCommand;
import seedu.address.logic.commands.RescheduleCommand;
import seedu.address.logic.commands.ScheduleCommand;
import seedu.address.logic.commands.SortAppointmentCommand;
import seedu.address.logic.commands.SortClientCommand;
import seedu.address.logic.commands.SortPetCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.UnscheduleCommand;
import seedu.address.logic.parser.Prefix;

//@@author jonathanwj

/**
 * the main AutoCompleteManager of the application
 */
public class AutoCompleteManager {

    private static final String LIST_CLIENT_PREFIX = " client";
    private static final String LIST_VETTECH_PREFIX = " vettech";
    private static final String LIST_PET_PREFIX = " pet";
    private static final String EMPTY_STRING = "";
    private Trie commandTrie;
    private CommandParameterSyntaxHandler commandParameterSyntaxHandler;

    public AutoCompleteManager() {
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
        commandTrie.insertWord(RescheduleCommand.COMMAND_WORD);
        commandTrie.insertWord(UnscheduleCommand.COMMAND_WORD);
        commandTrie.insertWord(UndoCommand.COMMAND_WORD);

        commandTrie.insertWord(AddAppointmentToPetCommand.COMMAND_WORD);
        commandTrie.insertWord(RemoveAppointmentFromPetCommand.COMMAND_WORD);
        commandTrie.insertWord(AddVetTechToAppointmentCommand.COMMAND_WORD);
        commandTrie.insertWord(RemoveVetTechFromAppointmentCommand.COMMAND_WORD);

        commandTrie.insertWord(SortClientCommand.COMMAND_WORD);
        commandTrie.insertWord(SortAppointmentCommand.COMMAND_WORD);
        commandTrie.insertWord(SortPetCommand.COMMAND_WORD);

        commandTrie.insertWord(ListCommand.COMMAND_WORD);
        commandTrie.insertWord(ListAllCommand.COMMAND_WORD);
        commandTrie.insertWord(ListCommand.COMMAND_WORD + LIST_CLIENT_PREFIX);
        commandTrie.insertWord(ListCommand.COMMAND_WORD + LIST_VETTECH_PREFIX);
        commandTrie.insertWord(ListCommand.COMMAND_WORD + LIST_PET_PREFIX);

    }

    /**
     * Returns a sorted list of auto completed commands with prefix
     */
    public List<String> getAutoCompleteCommands(String commandPrefix) {
        requireNonNull(commandPrefix);
        return commandTrie.autoComplete(commandPrefix).stream()
                .sorted(Comparator.comparingInt(String::length))
                .collect(Collectors.toList());
    }

    /**
     * Returns the the next missing prefix parameter of the {@code inputText}
     * or an empty string if there is no next prefix
     */
    public String getAutoCompleteNextMissingParameter(String inputText) {
        requireNonNull(inputText);
        if (inputText.isEmpty()) {
            return EMPTY_STRING;
        }
        String command = inputText.split(" ")[0];

        ArrayList<Prefix> missingPrefixes = commandParameterSyntaxHandler.getMissingPrefixes(command, inputText);

        if (!missingPrefixes.isEmpty()) {
            return missingPrefixes.get(0).getPrefix();
        } else {
            return EMPTY_STRING;
        }

    }
}
