package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_APPT1;
import static seedu.address.logic.commands.CommandTestUtil.DESC_APPT2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_DATE1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_DURATION1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_TIME1;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.prepareRedoCommand;
import static seedu.address.logic.commands.CommandTestUtil.prepareUndoCommand;
import static seedu.address.logic.commands.CommandTestUtil.showAppointmentAtIndex;
import static seedu.address.testutil.TypicalAppointments.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_APPT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_APPT;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.RescheduleCommand.RescheduleAppointmentDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.appointment.Appointment;
import seedu.address.testutil.AppointmentBuilder;
import seedu.address.testutil.RescheduleAppointmentDescriptorBuilder;

//@@author Godxin-test
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditCommand.
 */
public class RescheduleCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        Appointment rescheduledAppointment = new AppointmentBuilder().build();
        RescheduleAppointmentDescriptor descriptor = new RescheduleAppointmentDescriptorBuilder(rescheduledAppointment)
                .build();
        RescheduleCommand rescheduleCommand = prepareCommand(INDEX_FIRST_APPT, descriptor);

        String expectedMessage = String.format(RescheduleCommand.MESSAGE_RESCHEDULE_APPOINTMENT_SUCCESS,
                rescheduledAppointment);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateAppointment(model.getFilteredAppointmentList().get(0), rescheduledAppointment);

        assertCommandSuccess(rescheduleCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
        Index indexLastAppointment = Index.fromOneBased(model.getFilteredAppointmentList().size());
        Appointment lastAppointment = model.getFilteredAppointmentList().get(indexLastAppointment.getZeroBased());

        AppointmentBuilder appointmentInList = new AppointmentBuilder(lastAppointment);
        Appointment rescheduledAppointment = appointmentInList.withDate(VALID_APPOINTMENT_DATE1)
                .withTime(VALID_APPOINTMENT_TIME1).withDuration(VALID_APPOINTMENT_DURATION1).build();

        RescheduleAppointmentDescriptor descriptor = new RescheduleAppointmentDescriptorBuilder()
                .withDate(VALID_APPOINTMENT_DATE1).withTime(VALID_APPOINTMENT_TIME1)
                .withDuration(VALID_APPOINTMENT_DURATION1).build();
        RescheduleCommand rescheduleCommand = prepareCommand(indexLastAppointment, descriptor);

        String expectedMessage = String.format(RescheduleCommand.MESSAGE_RESCHEDULE_APPOINTMENT_SUCCESS,
                rescheduledAppointment);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateAppointment(lastAppointment, rescheduledAppointment);

        assertCommandSuccess(rescheduleCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        RescheduleCommand rescheduleCommand = prepareCommand(INDEX_FIRST_APPT, new RescheduleAppointmentDescriptor());
        Appointment rescheduledAppointment = model.getFilteredAppointmentList().get(INDEX_FIRST_APPT.getZeroBased());

        String expectedMessage = String.format(RescheduleCommand.MESSAGE_RESCHEDULE_APPOINTMENT_SUCCESS,
                rescheduledAppointment);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(rescheduleCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws Exception {

        Appointment appointmentInFilteredList = model.getFilteredAppointmentList().get(INDEX_FIRST_APPT.getZeroBased());
        Appointment rescheduledAppointment = new AppointmentBuilder(appointmentInFilteredList)
                .withDate(VALID_APPOINTMENT_DATE1).build();

        RescheduleCommand rescheduleCommand = prepareCommand(INDEX_FIRST_APPT,
                new RescheduleAppointmentDescriptorBuilder().withDate(VALID_APPOINTMENT_DATE1).build());

        String expectedMessage = String.format(RescheduleCommand.MESSAGE_RESCHEDULE_APPOINTMENT_SUCCESS,
                rescheduledAppointment);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateAppointment(model.getFilteredAppointmentList().get(0), rescheduledAppointment);

        assertCommandSuccess(rescheduleCommand, model, expectedMessage, expectedModel);

        // reschedule date of an appointment
        appointmentInFilteredList = model.getFilteredAppointmentList().get(INDEX_FIRST_APPT.getZeroBased());
        Appointment rescheduledAnotherAppointment = new AppointmentBuilder(appointmentInFilteredList)
                .withDate(VALID_APPOINTMENT_DATE1).build();
        rescheduleCommand = prepareCommand(INDEX_FIRST_APPT,
                new RescheduleAppointmentDescriptorBuilder().withDate(VALID_APPOINTMENT_DATE1).build());

        expectedMessage = String.format(RescheduleCommand.MESSAGE_RESCHEDULE_APPOINTMENT_SUCCESS,
                rescheduledAnotherAppointment);

        expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateAppointment(model.getFilteredAppointmentList().get(0), rescheduledAnotherAppointment);

        assertCommandSuccess(rescheduleCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidAppointmentIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredAppointmentList().size() + 1);
        RescheduleAppointmentDescriptor descriptor = new RescheduleAppointmentDescriptorBuilder()
                .withDate(VALID_APPOINTMENT_DATE1).build();
        RescheduleCommand rescheduleCommand = prepareCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(rescheduleCommand, model, Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidAppointmentIndexFilteredList_failure() {
        showAppointmentAtIndex(model, INDEX_FIRST_APPT);

        Index outOfBoundIndex = INDEX_SECOND_APPT;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getAppointmentList().size());

        RescheduleCommand rescheduleCommand = prepareCommand(outOfBoundIndex,
                new RescheduleAppointmentDescriptorBuilder().withDate(VALID_APPOINTMENT_DATE1).build());

        assertCommandFailure(rescheduleCommand, model, Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Appointment rescheduledAppointment = new AppointmentBuilder().build();
        Appointment appointmentToReschedule = model.getFilteredAppointmentList().get(INDEX_FIRST_APPT.getZeroBased());
        RescheduleAppointmentDescriptor descriptor = new RescheduleAppointmentDescriptorBuilder(rescheduledAppointment)
                .build();
        RescheduleCommand rescheduleCommand = prepareCommand(INDEX_FIRST_APPT, descriptor);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        // edit -> first appointment rescheduled
        rescheduleCommand.execute();
        undoRedoStack.push(rescheduleCommand);

        // undo -> reverts addressbook back to previous state and filtered appointment list to show all appointments
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first appointment rescheduled again
        expectedModel.updateAppointment(appointmentToReschedule, rescheduledAppointment);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredAppointmentList().size() + 1);
        RescheduleAppointmentDescriptor descriptor = new RescheduleAppointmentDescriptorBuilder()
                .withDate(VALID_APPOINTMENT_DATE1).build();
        RescheduleCommand rescheduleCommand = prepareCommand(outOfBoundIndex, descriptor);

        // execution failed -> rescheduleCommand not pushed into undoRedoStack
        assertCommandFailure(rescheduleCommand, model, Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Reschedules an {@code Appointment} from a filtered list.
     * 2. Undo the reschedule.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously rescheduled appointment
     * in the unfiltered list is different from the index at the filtered list.
     * 4. Redo the reschedule. This ensures {@code RedoCommand} reschedules the appointment object
     * regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameAppointmentEdited() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Appointment rescheduledAppointment = new AppointmentBuilder().build();
        RescheduleAppointmentDescriptor descriptor = new RescheduleAppointmentDescriptorBuilder(rescheduledAppointment)
                .build();
        RescheduleCommand rescheduleCommand = prepareCommand(INDEX_FIRST_APPT, descriptor);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        showAppointmentAtIndex(model, INDEX_SECOND_APPT);
        Appointment appointmentToReschedule = model.getFilteredAppointmentList().get(INDEX_FIRST_APPT.getZeroBased());
        // reschedule -> reschedules the second appointment in unfiltered appointment list /
        // first appointment in filtered appointment list
        rescheduleCommand.execute();
        undoRedoStack.push(rescheduleCommand);

        // undo -> reverts addressbook back to previous state and filtered appointment list to show all appointments
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        expectedModel.updateAppointment(appointmentToReschedule, rescheduledAppointment);
        assertNotEquals(model.getFilteredAppointmentList().get(INDEX_FIRST_APPT.getZeroBased()),
                appointmentToReschedule);
        // redo -> reschedules the same second appointment in unfiltered appointment list
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() throws Exception {
        final RescheduleCommand standardCommand = prepareCommand(INDEX_FIRST_APPT, DESC_APPT1);

        // same values -> returns true
        RescheduleAppointmentDescriptor copyDescriptor = new RescheduleAppointmentDescriptor(DESC_APPT1);
        RescheduleCommand commandWithSameValues = prepareCommand(INDEX_FIRST_APPT, copyDescriptor);
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
        assertFalse(standardCommand.equals(new RescheduleCommand(INDEX_SECOND_APPT, DESC_APPT1)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new RescheduleCommand(INDEX_FIRST_APPT, DESC_APPT2)));
    }

    /**
     * Returns an {@code RescheduleCommand} with parameters {@code index} and {@code descriptor}
     */
    private RescheduleCommand prepareCommand(Index index, RescheduleAppointmentDescriptor descriptor) {
        RescheduleCommand rescheduleCommand = new RescheduleCommand(index, descriptor);
        rescheduleCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return rescheduleCommand;
    }
}
