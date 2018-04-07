package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_INDEX;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_APPOINTMENT;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PETS;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.NewApptAvailableEvent;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.exceptions.AppointmentHasBeenTakenException;
import seedu.address.model.appointment.exceptions.AppointmentNotFoundException;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;
import seedu.address.model.association.exceptions.ClientPetAssociationNotFoundException;
import seedu.address.model.association.exceptions.PetAlreadyHasAppointmentException;
import seedu.address.model.pet.Pet;

//@@author md-azsa
/**
 * Adds an appointment to the pet object.
 */
public class AddAppointmentToPetCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "addappttopet";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an appointment to a pet "
            + "by the index number used in the last pet and last appointment listing.\n"
            + "Parameters: "
            + PREFIX_APPOINTMENT_INDEX + "APPOINTMENT_INDEX "
            + PREFIX_PET_INDEX + "PET_INDEX\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_APPOINTMENT_INDEX + "1 "
            + PREFIX_PET_INDEX + "2";

    public static final String MESSAGE_ADD_APPOINTMENT_TO_PET_SUCCESS = "Added Appointment to Pet:\n%1$s\n>> %2$s";
    public static final String MESSAGE_PET_HAS_APPOINTMENT = "Pet already has an appointment.";
    public static final String MESSAGE_PET_DOES_NOT_HAVE_OWNER = "This pet does not have an owner yet!";

    private final Index appointmentIndex;
    private final Index petIndex;

    private Optional<Appointment> appointment;
    private Optional<Pet> pet;

    /**
     * @parem appointmentIndex of the appointment in the filtered appointment list to add
     * @param petIndex of the pet in the filtered pet list to add to.
     */
    public AddAppointmentToPetCommand(Index appointmentIndex, Index petIndex) {
        requireAllNonNull(appointmentIndex, petIndex);

        this.appointmentIndex = appointmentIndex;
        this.petIndex = petIndex;

        appointment = Optional.empty();
        pet = Optional.empty();
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireAllNonNull(model, pet.get(), appointment.get());
        try {
            model.addAppointmentToPet(appointment.get(), pet.get());
        } catch (PetAlreadyHasAppointmentException e) {
            throw new CommandException(MESSAGE_PET_HAS_APPOINTMENT);
        } catch (ClientPetAssociationNotFoundException e) {
            throw new CommandException(MESSAGE_PET_DOES_NOT_HAVE_OWNER);
        } catch (DuplicateAppointmentException e) {
            throw new CommandException(ScheduleCommand.MESSAGE_DUPLICATE_APPOINTMENT);
        } catch (AppointmentNotFoundException e) {
            throw new CommandException(Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
        } catch (AppointmentHasBeenTakenException e) {
            throw new CommandException(Messages.MESSAGE_APPOINTMENT_TAKEN);
        }
        return new CommandResult(String.format(MESSAGE_ADD_APPOINTMENT_TO_PET_SUCCESS, appointment.get(), pet.get()));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Appointment> lastShownAppointmentList = model.getFilteredAppointmentList();
        List<Pet> lastShownPetList = model.getFilteredPetList();

        if (appointmentIndex.getZeroBased() >= lastShownAppointmentList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
        }
        if (petIndex.getZeroBased() >= lastShownPetList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PET_INDEX);
        }

        appointment = Optional.of(lastShownAppointmentList.get(appointmentIndex.getZeroBased()));
        pet = Optional.of(lastShownPetList.get(petIndex.getZeroBased()));
        model.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENT);
        model.updateFilteredPetList(PREDICATE_SHOW_ALL_PETS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AddAppointmentToPetCommand)) {
            return false;
        }

        AddAppointmentToPetCommand aatpc = (AddAppointmentToPetCommand) other;
        return appointmentIndex.equals(aatpc.appointmentIndex)
                && petIndex.equals(aatpc.petIndex)
                && appointment.equals(aatpc.appointment)
                && pet.equals(aatpc.pet);
    }
}
