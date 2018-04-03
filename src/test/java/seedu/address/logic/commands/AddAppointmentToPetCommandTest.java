package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.prepareRedoCommand;
import static seedu.address.logic.commands.CommandTestUtil.prepareUndoCommand;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CLIENTS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PETS;
import static seedu.address.testutil.TypicalAddressBook.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_APPT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PET;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_APPT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PET;

import org.junit.Before;
import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.association.exceptions.ClientAlreadyOwnsPetException;
import seedu.address.model.association.exceptions.PetAlreadyHasOwnerException;
import seedu.address.model.pet.Pet;

/**
 * Contains tests for AddAppointmentToPetCommand
 * TODO: Add clientpetasccoiatioes
 */
public class AddAppointmentToPetCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Before
    public void setup() throws ClientAlreadyOwnsPetException, PetAlreadyHasOwnerException {
        model.addPetToClient(model.getAddressBook().getPetList().get(0),
                model.getAddressBook().getClientList().get(0));
        model.updateFilteredPetList(PREDICATE_SHOW_ALL_PETS);
        model.updateFilteredClientList(PREDICATE_SHOW_ALL_CLIENTS);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);

        Index outOfBoundIndexPet = Index.fromOneBased(model.getFilteredPetList().size() + 1);
        Index outOfBoundIndexAppt = Index.fromOneBased(model.getFilteredClientList().size() + 1);

        AddAppointmentToPetCommand aptcCommand = prepareCommand(outOfBoundIndexPet, outOfBoundIndexAppt);

        // execution failed -> editCommand not pushed into undoRedoStack
        assertCommandFailure(aptcCommand, model, Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void equals() throws Exception {
        final AddAppointmentToPetCommand command = prepareCommand(INDEX_FIRST_APPT, INDEX_FIRST_PET);

        // Same values -> true
        AddAppointmentToPetCommand commandWithSameValues = prepareCommand(INDEX_FIRST_APPT, INDEX_FIRST_PET);
        assertTrue(command.equals(commandWithSameValues));

        // Same objects -> true
        assertTrue(command.equals(command));

        // One command being preprocessed -> false
        commandWithSameValues.preprocessUndoableCommand();
        assertFalse(command.equals(commandWithSameValues));

        // null -> false
        assertFalse(command.equals(null));

        // different commands -> false
        assertFalse(command.equals(new ClearCommand()));

        // different index -> false
        AddAppointmentToPetCommand differentIndexCommand = prepareCommand(INDEX_SECOND_APPT, INDEX_SECOND_PET);
        assertFalse(command.equals(differentIndexCommand));
    }

    @Test
    public void execute_appointmentHasBeenBooked_throwsCommandException() throws Exception {
        AddAppointmentToPetCommand command = prepareCommand(INDEX_FIRST_APPT, INDEX_FIRST_PET);
        command.execute();
        command = prepareCommand(INDEX_FIRST_APPT, INDEX_SECOND_PET);

        assertCommandFailure(command, model, Messages.MESSAGE_APPOINTMENT_TAKEN);
    }

    @Test
    public void execute_clientPetAssociationNotFound_throwsCommandException() throws Exception {
        AddAppointmentToPetCommand command = prepareCommand(INDEX_FIRST_APPT, INDEX_SECOND_PET);
        assertCommandFailure(command, model, AddAppointmentToPetCommand.MESSAGE_PET_DOES_NOT_HAVE_OWNER);
    }

    @Test
    public void execute_addAppointmentToPet_success() throws Exception {
        Appointment appointmentInList = model.getFilteredAppointmentList().get(INDEX_FIRST_APPT.getZeroBased());
        Pet petInList = model.getFilteredPetList().get(INDEX_FIRST_PET.getZeroBased());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addAppointmentToPet(appointmentInList, petInList);

        AddAppointmentToPetCommand command = prepareCommand(INDEX_FIRST_APPT, INDEX_FIRST_PET);
        String expectedMessage = String.format(AddAppointmentToPetCommand.MESSAGE_ADD_APPOINTMENT_TO_PET_SUCCESS,
                appointmentInList, petInList);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }


    /**
     * Returns an {@code AddAppointmentToPetCommand} wth parameters
     * of appointment {@code index} and of pet {@code index}
     */
    private AddAppointmentToPetCommand prepareCommand(Index appointmentIndex, Index petIndex) {
        AddAppointmentToPetCommand addappt = new AddAppointmentToPetCommand(appointmentIndex, petIndex);
        addappt.setData(model, new CommandHistory(), new UndoRedoStack());
        return addappt;
    }
}
