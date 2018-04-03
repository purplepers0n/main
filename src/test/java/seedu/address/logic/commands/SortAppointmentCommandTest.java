package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalAddressBook.getTypicalAddressBook;
import static seedu.address.testutil.TypicalAddressBook.getTypicalAddressBookWithNoAppointments;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests for sorting the appointment list.
 */
public class SortAppointmentCommandTest {

    @Rule
    public ExpectedException error = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model modelWithNoAppointments = new ModelManager(getTypicalAddressBookWithNoAppointments(), new UserPrefs());

    @Test
    public void sortEmptyAppointments() throws Exception {
        error.expect(CommandException.class);
        prepareCommand(modelWithNoAppointments).execute();
    }

    @Test
    public void sortAppointment_success() throws Exception {
        Model modelSorted = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        modelSorted.sortAppointmentList();
        SortAppointmentCommand command = prepareCommand(model);
        String expectedMessage = SortAppointmentCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedMessage, modelSorted);
    }

    @Test
    public void sortEmptyAppointment_fail() throws Exception {
        SortAppointmentCommand command = prepareCommand(modelWithNoAppointments);
        assertCommandFailure(command, modelWithNoAppointments, Messages.MESSAGE_APPOINTMENT_LIST_EMPTY);
    }

    @Test
    public void equals() throws Exception {
        SortAppointmentCommand command = prepareCommand(model);
        command.execute();

        // Same objects -> return true
        assertEquals(command, command);
        // Different types -> return false
        assertFalse(command.equals(new ClearCommand()));
    }

    private SortAppointmentCommand prepareCommand(Model model) {
        SortAppointmentCommand command = new SortAppointmentCommand();
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

}
