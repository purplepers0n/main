package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.NewApptAvailableEvent;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.exceptions.AppointmentNotFoundException;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;
import seedu.address.model.vettechnician.exceptions.VetTechnicianNotFoundException;

//@@author jonathanwj-reused
/**
 * removes the vet from appointment identified using it's last displayed index from the program
 */
public class RemoveVetTechFromAppointmentCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "removevettechfromappt";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": removes the vet technician from appointment identified by the index number"
            + " used in the last appointment listing\n"
            + "Parameters: INDEX (must be a postive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_REMOVE_VET_FROM_APPT_SUCCESS = "Removed vet technician from: %1$s";
    public static final String MESSAGE_APPOINTMENT_DOES_NOT_HAVE_TECH = "Appointment "
            + "does not have a vet technician";

    private final Index targetIndex;

    private Appointment apptToRemoveVetFrom;

    public RemoveVetTechFromAppointmentCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(apptToRemoveVetFrom);
        try {
            model.removeVetTechFromAppointent(apptToRemoveVetFrom);
        } catch (DuplicateAppointmentException e) {
            throw new AssertionError("The target appointment cannot be a duplicate");
        } catch (AppointmentNotFoundException e) {
            throw new AssertionError("The target appointment cannot be missing");
        } catch (VetTechnicianNotFoundException e) {
            throw new CommandException(MESSAGE_APPOINTMENT_DOES_NOT_HAVE_TECH);
        }
        return new CommandResult(String.format(MESSAGE_REMOVE_VET_FROM_APPT_SUCCESS, apptToRemoveVetFrom));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Appointment> lastShownList = model.getFilteredAppointmentList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
        }

        apptToRemoveVetFrom = lastShownList.get(targetIndex.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof RemoveVetTechFromAppointmentCommand
                && this.targetIndex.equals(((RemoveVetTechFromAppointmentCommand) other).targetIndex)
                && Objects.equals(this.apptToRemoveVetFrom, ((RemoveVetTechFromAppointmentCommand) other)
                .apptToRemoveVetFrom));
    }
}
