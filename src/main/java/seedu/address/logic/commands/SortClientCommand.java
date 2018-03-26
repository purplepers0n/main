package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Sorts the client list
 */
public class SortClientCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "sortc";

    public static final String MESSAGE_SUCCESS = "Client list sorted";


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        model.sortClientList();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
