package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Sorts the appointment list.
 */
public class SortAppointmentCommand extends  UndoableCommand {

    public static final String COMMAND_WORD = "sortappt";
    public static final String MESSAGE_SUCCESS = "Appointment list successfully sorted";

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        model.sortAppointmentList();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
