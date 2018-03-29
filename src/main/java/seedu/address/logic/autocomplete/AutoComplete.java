package seedu.address.logic.autocomplete;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.logic.commands.AddAppointmentToPetCommand;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddPetCommand;
import seedu.address.logic.commands.AddPetToClientCommand;
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
import seedu.address.logic.commands.RemovePetFromClientCommand;
import seedu.address.logic.commands.ScheduleCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.UndoCommand;

/**
 * the main AutoCompleteManager of the application
 */
public class AutoComplete {

    private Trie commandTrie;

    public AutoComplete() {
        commandTrie = new Trie();
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
        commandTrie.insertWord(ListCommand.COMMAND_WORD);
        commandTrie.insertWord(RedoCommand.COMMAND_WORD);
        commandTrie.insertWord(ScheduleCommand.COMMAND_WORD);
        commandTrie.insertWord(SelectCommand.COMMAND_WORD);
        commandTrie.insertWord(UndoCommand.COMMAND_WORD);
        commandTrie.insertWord(AddAppointmentToPetCommand.COMMAND_WORD);
        commandTrie.insertWord(AddPetToClientCommand.COMMAND_WORD);
        commandTrie.insertWord(RemovePetFromClientCommand.COMMAND_WORD);
        commandTrie.insertWord(RemoveAppointmentFromPetCommand.COMMAND_WORD);
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
}
