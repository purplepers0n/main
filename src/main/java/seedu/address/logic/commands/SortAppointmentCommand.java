package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.appointment.exceptions.AppointmentListIsEmptyException;

/**
 * Sorts the appointment list.
 */
public class SortAppointmentCommand extends  UndoableCommand {

    public static final String COMMAND_WORD = "sortappt";
    public static final String MESSAGE_SUCCESS = "Appointment list successfully sorted";

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            model.sortAppointmentList();
        } catch (AppointmentListIsEmptyException e) {
            throw new CommandException(Messages.MESSAGE_APPOINTMENT_LIST_EMPTY);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
