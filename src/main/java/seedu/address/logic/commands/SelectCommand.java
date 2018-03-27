package seedu.address.logic.commands;

import java.util.List;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Selects a person identified using it's last displayed index from the address book.
 */
public class SelectCommand extends Command {

    public static final String COMMAND_WORD = "select";
    public static final String COMMAND_ALIS = "sel";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the person identified by the index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_PERSON_SUCCESS = "Selected Person: %1$s";
    public static final String MESSAGE_SELECT_PET_SUCCESS = "Selected Pet: %1$s";

    private final Index targetIndex;
    private int currList = 0; //default is on client list upon opening app

    public SelectCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    public void setCurrentList() {
        this.currList = model.getCurrentList();
    }

    @Override
    public CommandResult execute() throws CommandException {
        List<?> lastShownList;

        setCurrentList();

        if (currList == 0) {
            lastShownList = model.getFilteredClientList();
        } else if (currList == 1) {
            lastShownList = model.getFilteredPetList();
        } else if (currList == 2) {
            lastShownList = model.getFilteredVetTechnicianList();
        } else {
            throw new CommandException("Not currently on a list that 'select' command can work");
        }

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        EventsCenter.getInstance().post(new JumpToListRequestEvent(targetIndex, currList));
        if (currList == 1) {
            return new CommandResult(String.format(MESSAGE_SELECT_PET_SUCCESS, targetIndex.getOneBased()));
        }
        return new CommandResult(String.format(MESSAGE_SELECT_PERSON_SUCCESS, targetIndex.getOneBased()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SelectCommand // instanceof handles nulls
                && this.targetIndex.equals(((SelectCommand) other).targetIndex)); // state check
    }
}
