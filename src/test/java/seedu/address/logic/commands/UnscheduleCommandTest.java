package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.prepareRedoCommand;
import static seedu.address.logic.commands.CommandTestUtil.prepareUndoCommand;
import static seedu.address.logic.commands.CommandTestUtil.showAppointmentAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_APPT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_APPT;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_APPT;

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
import seedu.address.testutil.TypicalAddressBook;

//@@author md-azsa
/**
 * Contains integration tests unit for {@code UnscheduleCommand}.
 */
public class UnscheduleCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private Model model = new ModelManager(TypicalAddressBook.getTypicalAddressBook(),
            new UserPrefs());

    @Test
    public void execute_unscheduleCommand_throwsCOmmandException() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
        prepareCommand(INDEX_THIRD_APPT).execute();
    }

    @Test
    public void execute_unscheduleCommand_success() throws Exception {
        Appointment appointmentInFilteredList = model.getFilteredAppointmentList().get(INDEX_FIRST_APPT.getZeroBased());

        UnscheduleCommand command = prepareCommand(INDEX_FIRST_APPT);

        String expectedMessage = String.format(UnscheduleCommand.MESSAGE_UNSCHEDULE_APPOINTMENT_SUCCESS,
                appointmentInFilteredList);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()),
                new UserPrefs());
        expectedModel.unscheduleAppointment(model.getFilteredAppointmentList().get(0));
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_failure() {
        showAppointmentAtIndex(model, INDEX_FIRST_APPT);
        Index outOfBOunds = INDEX_SECOND_APPT;

        assertTrue(outOfBOunds.getZeroBased() < model.getAddressBook().getAppointmentList().size());

        UnscheduleCommand command = prepareCommand(outOfBOunds);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfileteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Appointment appointmentToDelete = model.getFilteredAppointmentList().get(INDEX_FIRST_APPT.getZeroBased());
        UnscheduleCommand command = prepareCommand(INDEX_FIRST_APPT);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        // delete -> first appt unschedule
        command.execute();
        undoRedoStack.push(command);

        // undo -> reverts
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo
        expectedModel.unscheduleAppointment(appointmentToDelete);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }


    @Test
    public void equals() throws Exception {
        final UnscheduleCommand standardCommand = prepareCommand(INDEX_FIRST_APPT);

        // same values -> true
        UnscheduleCommand sameValueCommand = prepareCommand(INDEX_FIRST_APPT);
        assertTrue(standardCommand.equals(standardCommand));
        assertTrue(standardCommand.equals(sameValueCommand));

        // preprocessed
        sameValueCommand.preprocessUndoableCommand();
        assertFalse(sameValueCommand.equals(standardCommand));

        // null -> false
        assertFalse(standardCommand.equals(null));

        // different type -> false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index
        assertFalse(standardCommand.equals(new UnscheduleCommand(INDEX_SECOND_APPT)));
    }

    /**
     * Returns an {@code UnscheduleCommand} object with param.
     */
    private UnscheduleCommand prepareCommand(Index appointmentIndex) {
        UnscheduleCommand command = new UnscheduleCommand(appointmentIndex);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

}
