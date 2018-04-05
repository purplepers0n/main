package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.association.exceptions.ClientPetAssociationListEmptyException;

//@@author md-azsa
/**
 * Sorts the pet list.
 */
public class SortPetCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "sortp";
    public static final String MESSAGE_SUCCESS = "Pet list successfully sorted";

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            model.sortPetList();
        } catch (ClientPetAssociationListEmptyException e) {
            throw new CommandException(Messages.MESSAGE_CLIENTPETLIST_EMPTY);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
