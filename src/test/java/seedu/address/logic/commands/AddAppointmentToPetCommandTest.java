package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.prepareRedoCommand;
import static seedu.address.logic.commands.CommandTestUtil.prepareUndoCommand;
import static seedu.address.testutil.TypicalAddressBook.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains tests for AddAppointmentToPetCommand
 */
public class AddAppointmentToPetCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

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
