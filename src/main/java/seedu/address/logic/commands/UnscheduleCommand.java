package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.exceptions.AppointmentCloseToNextException;
import seedu.address.model.appointment.exceptions.AppointmentCloseToPreviousException;
import seedu.address.model.appointment.exceptions.AppointmentListIsEmptyException;
import seedu.address.model.appointment.exceptions.AppointmentNotFoundException;

//@@author md-azsa
/**
 * Deletes an appointment identifiedusing it's last displayed index from the program.
 */
public class UnscheduleCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "unschedule";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the appointment identified by the index number used in the appointment listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNSCHEDULE_APPOINTMENT_SUCCESS =
            "Unscheduled appointment: %1$s";

    private final Index targetIndex;

    private Appointment appointmentToDelete;

    public UnscheduleCommand (Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(appointmentToDelete);
        try {
            model.unscheduleAppointment(appointmentToDelete);
        } catch (AppointmentNotFoundException e) {
            throw new AssertionError("The target cannot be missing.");
        } catch (AppointmentListIsEmptyException e) {
            throw new AssertionError("Appointment cannot be missing");
        } catch (AppointmentCloseToPreviousException ape) {
            throw new AssertionError("New appointment is too close to the previous one");
        } catch (AppointmentCloseToNextException ape) {
            throw new AssertionError("New appointment is too close to the next one");
        }
        return new CommandResult(String.format(MESSAGE_UNSCHEDULE_APPOINTMENT_SUCCESS, appointmentToDelete));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Appointment> lastShownList = model.getFilteredAppointmentList();

        if (lastShownList.isEmpty()) {
            throw new CommandException(Messages.MESSAGE_APPOINTMENT_LIST_EMPTY);
        } else if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
        }

        appointmentToDelete = lastShownList.get(targetIndex.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof UnscheduleCommand
                && this.targetIndex.equals(((UnscheduleCommand) other).targetIndex)
                && Objects.equals(this.appointmentToDelete, ((UnscheduleCommand) other).appointmentToDelete));
    }
}
