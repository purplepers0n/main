package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.exceptions.PersonsListIsEmptyException;

//@@author md-azsa
/**
 * Sorts the client list
 */
public class SortClientCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "sortc";

    public static final String MESSAGE_SUCCESS = "Client list sorted";


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            model.sortClientList();
        } catch (PersonsListIsEmptyException e) {
            throw new CommandException(Messages.MESSAGE_PERSONSLIST_EMPTY);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
