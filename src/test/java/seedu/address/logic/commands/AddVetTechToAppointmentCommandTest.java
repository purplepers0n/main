package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.prepareRedoCommand;
import static seedu.address.logic.commands.CommandTestUtil.prepareUndoCommand;
import static seedu.address.logic.commands.CommandTestUtil.showAppointmentAtIndex;
import static seedu.address.logic.commands.CommandTestUtil.showVetTechnicianAtIndex;
import static seedu.address.testutil.TypicalAddressBook.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.vettechnician.VetTechnician;

//@@author jonathanwj-reused
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand)
 * and unit tests for AddVetTechToAppointmentCommand.
 */
public class AddVetTechToAppointmentCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_vetTechFilteredList_success() throws Exception {
        showVetTechnicianAtIndex(model, INDEX_FIRST);
        showAppointmentAtIndex(model, INDEX_FIRST);

        VetTechnician vetTechInFilteredList = model.getFilteredVetTechnicianList().get(INDEX_FIRST.getZeroBased());
        Appointment appointmentInFilteredList = model.getFilteredAppointmentList().get(INDEX_FIRST.getZeroBased());

        AddVetTechToAppointmentCommand avttcCommand = prepareCommand(INDEX_FIRST, INDEX_FIRST);

        String expectedMessage = String.format(AddVetTechToAppointmentCommand.MESSAGE_ADD_TECH_TO_APPOINTMENT_SUCCESS,
                vetTechInFilteredList, appointmentInFilteredList);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.addVetTechToAppointment(model.getFilteredVetTechnicianList().get(0),
                model.getFilteredAppointmentList().get(0));

        assertCommandSuccess(avttcCommand, model, expectedMessage, expectedModel);
    }


    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndexVetTechnician = Index.fromOneBased(model.getFilteredVetTechnicianList().size() + 1);
        Index outOfBoundIndexAppointment = Index.fromOneBased(model.getFilteredAppointmentList().size() + 1);

        AddVetTechToAppointmentCommand avttcCommand = prepareCommand(outOfBoundIndexVetTechnician, INDEX_FIRST);

        assertCommandFailure(avttcCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        avttcCommand = prepareCommand(INDEX_FIRST, outOfBoundIndexAppointment);

        assertCommandFailure(avttcCommand, model, Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showAppointmentAtIndex(model, INDEX_FIRST);
        Index outOfBoundIndexAppointment = INDEX_SECOND;

        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndexAppointment.getZeroBased() < model.getAddressBook().getAppointmentList().size());

        AddVetTechToAppointmentCommand avttcCommand = prepareCommand(INDEX_FIRST, outOfBoundIndexAppointment);

        assertCommandFailure(avttcCommand, model, Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);

        showVetTechnicianAtIndex(model, INDEX_FIRST);
        Index outOfBoundIndexVetTechnician = INDEX_SECOND;

        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndexVetTechnician.getZeroBased() < model.getAddressBook().getAppointmentList().size());

        avttcCommand = prepareCommand(outOfBoundIndexVetTechnician, INDEX_FIRST);

        assertCommandFailure(avttcCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);

        AddVetTechToAppointmentCommand avttcCommand = prepareCommand(INDEX_FIRST, INDEX_FIRST);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        // add -> vetTech added to appointment
        avttcCommand.execute();
        undoRedoStack.push(avttcCommand);

        // undo -> reverts addressbook back to previous state and filtered lists to show all
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> add vetTech back to appointment
        expectedModel.addVetTechToAppointment(model.getFilteredVetTechnicianList().get(INDEX_FIRST.getZeroBased()),
                model.getFilteredAppointmentList().get(INDEX_FIRST.getZeroBased()));
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndexVetTechnician = Index.fromOneBased(model.getFilteredVetTechnicianList().size() + 1);
        Index outOfBoundIndexAppointment = Index.fromOneBased(model.getFilteredAppointmentList().size() + 1);

        AddVetTechToAppointmentCommand avttcCommand = prepareCommand(outOfBoundIndexVetTechnician,
                outOfBoundIndexAppointment);

        // execution failed -> editCommand not pushed into undoRedoStack
        assertCommandFailure(avttcCommand, model, Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Adss a {@code VetTechnician} To a {@code Appointment} from filtered lists.
     * 2. Undo the command.
     * 3. The unfiltered lists should be shown now. Verify that the index of the previously edited appointment in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the command. This ensures {@code RedoCommand} adds the vetTech to appointment object regardless
     * of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_samePersonEdited() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);

        AddVetTechToAppointmentCommand avttcCommand = prepareCommand(INDEX_FIRST, INDEX_FIRST);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        showVetTechnicianAtIndex(model, INDEX_SECOND);
        showAppointmentAtIndex(model, INDEX_SECOND);
        VetTechnician vetTechToAdd = model.getFilteredVetTechnicianList().get(INDEX_FIRST.getZeroBased());
        Appointment appointmentToAddVetTechnicianTo = model.getFilteredAppointmentList()
                .get(INDEX_FIRST.getZeroBased());
        /* add -> add first appointment in filtered appointment list
           and the first vetTech in filtered vetTech list
        */
        avttcCommand.execute();
        undoRedoStack.push(avttcCommand);

        /* undo -> reverts addressbook back to previous state and filtered
           person list to show all appointments and vetTechs
        */
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        assertNotEquals(model.getFilteredVetTechnicianList().get(INDEX_FIRST.getZeroBased()), vetTechToAdd);
        assertNotEquals(model.getFilteredAppointmentList().get(INDEX_FIRST.getZeroBased()),
                appointmentToAddVetTechnicianTo);

        // redo -> add the same vetTech to appointment in unfiltered lists
        expectedModel.addVetTechToAppointment(vetTechToAdd, appointmentToAddVetTechnicianTo);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_addVetTechToAppointment_success() throws Exception {
        VetTechnician vetTechInFilteredList = model.getFilteredVetTechnicianList().get(INDEX_FIRST.getZeroBased());
        Appointment appointmentInFilteredList = model.getFilteredAppointmentList().get(INDEX_FIRST.getZeroBased());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addVetTechToAppointment(vetTechInFilteredList, appointmentInFilteredList);

        AddVetTechToAppointmentCommand avttcCommand = prepareCommand(INDEX_FIRST, INDEX_FIRST);
        String expectedMessage = String.format(AddVetTechToAppointmentCommand.MESSAGE_ADD_TECH_TO_APPOINTMENT_SUCCESS,
                vetTechInFilteredList, appointmentInFilteredList);

        assertCommandSuccess(avttcCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_appointmentAlreadyHasVetTechnician_throwsCommandException() throws Exception {
        AddVetTechToAppointmentCommand avttcCommand = prepareCommand(INDEX_FIRST, INDEX_FIRST);
        avttcCommand.execute();

        assertCommandFailure(avttcCommand, model, AddVetTechToAppointmentCommand.MESSAGE_APPOINTMENT_HAS_TECH);
    }

    @Test
    public void equals() throws Exception {
        final AddVetTechToAppointmentCommand standardCommand = prepareCommand(INDEX_FIRST, INDEX_FIRST);

        // same values -> returns true
        AddVetTechToAppointmentCommand commandWithSameValues = prepareCommand(INDEX_FIRST, INDEX_FIRST);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // one command preprocessed when previously equal -> returns false
        commandWithSameValues.preprocessUndoableCommand();
        assertFalse(standardCommand.equals(commandWithSameValues));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new AddVetTechToAppointmentCommand(INDEX_SECOND, INDEX_SECOND)));
    }

    /**
     * Returns an {@code AddVetTechToAppointmentCommand} with parameters vetTech {@code index}
     * and appointment {@code index}
     */
    private AddVetTechToAppointmentCommand prepareCommand(Index vetTechIndex, Index appointmentIndex) {
        AddVetTechToAppointmentCommand avttcCommand = new AddVetTechToAppointmentCommand(vetTechIndex,
                appointmentIndex);
        avttcCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return avttcCommand;
    }
}
