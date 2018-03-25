package seedu.address.logic.commands.exceptions;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.UndoableCommand;

/**
 * Sorts the client list
 */
public class SortClientCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "sortc";

    public static final String MESSAGE_USAGE = "This is the message usage placeholder";

    public static final String MESSAGE_SUCCESS = "Client list sorted";


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        model.sortClientList();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
