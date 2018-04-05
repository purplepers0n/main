package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showAppointmentAtIndex;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_APPOINTMENT;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CLIENTS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PET;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PETS;
import static seedu.address.testutil.TypicalAddressBook.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_APPT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_APPT;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.exceptions.AppointmentHasBeenTakenException;
import seedu.address.model.appointment.exceptions.AppointmentNotFoundException;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;
import seedu.address.model.association.exceptions.ClientAlreadyOwnsPetException;
import seedu.address.model.association.exceptions.ClientPetAssociationNotFoundException;
import seedu.address.model.association.exceptions.PetAlreadyHasAppointmentException;
import seedu.address.model.association.exceptions.PetAlreadyHasOwnerException;

//@@author md-azsa
/**
 * Contains integration test and unit tests for RemoveAppointmentFromPetCommand
 */
public class RemoveAppointmentFromPetCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Before
    public void setup() throws PetAlreadyHasAppointmentException, ClientPetAssociationNotFoundException,
            DuplicateAppointmentException, AppointmentHasBeenTakenException,
            AppointmentNotFoundException, ClientAlreadyOwnsPetException, PetAlreadyHasOwnerException {

        // set up association
        model.addPetToClient(model.getAddressBook().getPetList().get(0),
                model.getAddressBook().getClientList().get(0));
        model.updateFilteredPetList(PREDICATE_SHOW_ALL_PETS);
        model.updateFilteredClientList(PREDICATE_SHOW_ALL_CLIENTS);

        //adds appointment to association
        model.addAppointmentToPet(model.getAddressBook().getAppointmentList().get(0),
                model.getAddressBook().getPetList().get(0));
        model.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENT);
        model.updateFilteredPetList(PREDICATE_SHOW_ALL_PET);
    }

    @Test
    public void execute_removeAppointmentFromPet_throwsCommandException() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
        prepareCommand(INDEX_THIRD_PERSON).execute();
    }

    @Test
    public void execute_removeAppointmentFromPet_success() throws Exception {
        Appointment appointmentInFilteredList = model.getFilteredAppointmentList().get(INDEX_FIRST_APPT.getZeroBased());

        RemoveAppointmentFromPetCommand command = prepareCommand(INDEX_FIRST_APPT);

        String expectedMessage = String.format(RemoveAppointmentFromPetCommand.MESSAGE_REMOVE_APPOINTMENT_SUCCESS,
                appointmentInFilteredList);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()),
                new UserPrefs());
        expectedModel.removeAppointmentFromPet(model.getFilteredAppointmentList().get(0));
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_failure() {
        showAppointmentAtIndex(model, INDEX_FIRST_APPT);
        Index outOfBounds = INDEX_SECOND_APPT;

        assertTrue(outOfBounds.getZeroBased() < model.getAddressBook().getAppointmentList().size());

        RemoveAppointmentFromPetCommand command = prepareCommand(outOfBounds);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
    }

    @Test
    public void equals() throws Exception {
        final RemoveAppointmentFromPetCommand standardCommand = prepareCommand(INDEX_FIRST_APPT);

        // same values -> true
        RemoveAppointmentFromPetCommand sameValueCommand = prepareCommand(INDEX_FIRST_APPT);
        assertTrue(standardCommand.equals(sameValueCommand));
        assertTrue(standardCommand.equals(standardCommand));

        //preprocessed with undoable
        sameValueCommand.preprocessUndoableCommand();
        assertFalse(standardCommand.equals(sameValueCommand));

        // null
        assertFalse(standardCommand.equals(null));

        // different types
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new RemoveAppointmentFromPetCommand(INDEX_SECOND_APPT)));
    }

    /**
     * Returns an {@code RemoveAppointmentFromPetCommand} object with param
     */
    private RemoveAppointmentFromPetCommand prepareCommand(Index appointmentIndex) {
        RemoveAppointmentFromPetCommand command = new RemoveAppointmentFromPetCommand(appointmentIndex);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
