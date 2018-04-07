package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_INDEX;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_APPOINTMENT;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.exceptions.AppointmentDoesNotHavePetException;
import seedu.address.model.appointment.exceptions.AppointmentNotFoundException;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;

//@@author md-azsa
/**
 * Removes the appointment from a pet
 */
public class RemoveAppointmentFromPetCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "removeapptfrompet";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes an appointment from a pet "
            + "by the index number used din the last appointment listing.\n "
            + "Parameters: "
            + PREFIX_APPOINTMENT_INDEX + "APPOINTMENT_INDEX\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_APPOINTMENT_INDEX + "1";

    public static final String MESSAGE_REMOVE_APPOINTMENT_SUCCESS = "Removed appointment from pet: %1$s\n";

    private final Index appointmentIndex;

    private Optional<Appointment> appointment;

    /**
     * @param appointmentIndex of the appointment in the filtered appointment list
     */
    public RemoveAppointmentFromPetCommand(Index appointmentIndex) {
        requireNonNull(appointmentIndex);

        this.appointmentIndex = appointmentIndex;

        appointment = Optional.empty();
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireAllNonNull(model, appointment.get());
        try {
            model.removeAppointmentFromPet(appointment.get());
        } catch (AppointmentNotFoundException e) {
            throw new CommandException(Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
        } catch (DuplicateAppointmentException e) {
            throw new CommandException(ScheduleCommand.MESSAGE_DUPLICATE_APPOINTMENT);
        } catch (AppointmentDoesNotHavePetException e) {
            throw new CommandException(Messages.MESSAGE_APPOINTMENT_NO_PET);
        }
        return new CommandResult(String.format(MESSAGE_REMOVE_APPOINTMENT_SUCCESS, appointment.get()));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Appointment> lastShownAppointmentList = model.getFilteredAppointmentList();

        if (appointmentIndex.getZeroBased() >= lastShownAppointmentList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
        }

        appointment = Optional.of(lastShownAppointmentList.get(appointmentIndex.getZeroBased()));
        model.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENT);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof RemoveAppointmentFromPetCommand)) {
            return false;
        }

        RemoveAppointmentFromPetCommand e = (RemoveAppointmentFromPetCommand) other;
        return appointmentIndex.equals(e.appointmentIndex)
                && appointment.equals(e.appointment);
    }

}
