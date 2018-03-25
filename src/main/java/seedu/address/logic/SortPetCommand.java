package seedu.address.logic;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.UndoableCommand;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Sorts the pet list.
 */
public class SortPetCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "sortp";
    public static final String MESSAGE_SUCCESS = "Pet list successfully sorted";

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        model.sortPetList();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
