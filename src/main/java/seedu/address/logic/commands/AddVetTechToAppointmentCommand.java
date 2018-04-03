package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLIENT_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VETTECH_INDEX;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_APPOINTMENT;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_TECHNICIAN;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.exceptions.AppointmentAlreadyHasVetTechnicianException;
import seedu.address.model.appointment.exceptions.AppointmentNotFoundException;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;
import seedu.address.model.vettechnician.VetTechnician;

/**
 * Adds a vet technician to an appointment in the address book.
 */
public class AddVetTechToAppointmentCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "addvettechtoappointment";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Add a vet technician to a appointment "
            + "by the index number used in the last vet technician and appointment listing.\n"
            + "Parameters: "
            + PREFIX_VETTECH_INDEX + "VETTECH_INDEX "
            + PREFIX_CLIENT_INDEX + "APPOINTMENT_INDEX\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_VETTECH_INDEX + "1 " + PREFIX_APPOINTMENT_INDEX + "2";

    public static final String MESSAGE_ADD_TECH_TO_APPOINTMENT_SUCCESS = "Added vet technician To"
            + " appointment:\n%1$s\n>> %2$s";

    public static final String MESSAGE_DUPLICATE_APPOINTMENT = "Duplicate appointments";
    public static final String MESSAGE_APPOINTMENT_NOT_FOUND = "Appointment not found";
    public static final String MESSAGE_APPOINTMENT_HAS_TECH = "Appointment already has vet technician";

    private final Index vetTechIndex;
    private final Index appointmentIndex;

    private Optional<VetTechnician> vetTech;
    private Optional<Appointment> appointment;

    /**
     * @param vetTechIndex     of the vet tech in the filtered list to add
     * @param appointmentIndex of the appointment in the filtered list to add vet tech to
     */
    public AddVetTechToAppointmentCommand(Index vetTechIndex, Index appointmentIndex) {
        requireNonNull(vetTechIndex);
        requireNonNull(appointmentIndex);

        this.vetTechIndex = vetTechIndex;
        this.appointmentIndex = appointmentIndex;

        vetTech = Optional.empty();
        appointment = Optional.empty();
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        requireNonNull(vetTech.get());
        requireNonNull(appointment.get());
        try {
            model.addVetTechToAppointment(vetTech.get(), appointment.get());
        } catch (DuplicateAppointmentException e) {
            throw new CommandException(MESSAGE_DUPLICATE_APPOINTMENT);
        } catch (AppointmentNotFoundException e) {
            throw new CommandException(MESSAGE_APPOINTMENT_NOT_FOUND);
        } catch (AppointmentAlreadyHasVetTechnicianException e) {
            throw new CommandException(MESSAGE_APPOINTMENT_HAS_TECH);
        }

        return new CommandResult(String.format(MESSAGE_ADD_TECH_TO_APPOINTMENT_SUCCESS,
                vetTech.get(), appointment.get()));

    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<VetTechnician> lastShownListVetTech = model.getFilteredVetTechnicianList();
        List<Appointment> lastShownListAppointment = model.getFilteredAppointmentList();

        if (appointmentIndex.getZeroBased() >= lastShownListAppointment.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
        }
        if (vetTechIndex.getZeroBased() >= lastShownListVetTech.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        vetTech = Optional.of(lastShownListVetTech.get(vetTechIndex.getZeroBased()));
        appointment = Optional.of(lastShownListAppointment.get(appointmentIndex.getZeroBased()));
        model.updateFilteredVetTechnicianList(PREDICATE_SHOW_ALL_TECHNICIAN);
        model.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENT);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddVetTechToAppointmentCommand)) {
            return false;
        }

        // state check
        AddVetTechToAppointmentCommand e = (AddVetTechToAppointmentCommand) other;
        return vetTechIndex.equals(e.vetTechIndex)
                && appointmentIndex.equals(e.appointmentIndex)
                && vetTech.equals(e.vetTech)
                && appointment.equals(e.appointment);
    }

}
