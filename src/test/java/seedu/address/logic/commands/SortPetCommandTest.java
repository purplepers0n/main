package seedu.address.logic.commands;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalAddressBook.getTypicalAddressBook;
import static seedu.address.testutil.TypicalAddressBook.getTypicalAddressBookWithNoClientPetList;

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
 * Contains integration tests for sorting the client association list.
 */
public class SortPetCommandTest {

    @Rule
    public ExpectedException error = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model modelWithNoClientPetAssociationList =
            new ModelManager(getTypicalAddressBookWithNoClientPetList(), new UserPrefs());

    @Test
    public void sortEmptyList() throws Exception {
        error.expect(CommandException.class);
        prepareCommand(modelWithNoClientPetAssociationList).execute();
    }

    @Test
    public void sortClientPetList_success() throws Exception {
        Model modelSorted = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        modelSorted.sortPetList();

        SortPetCommand command = prepareCommand(model);
        String expectedMessage = SortPetCommand.MESSAGE_SUCCESS;

        assertCommandSuccess(command, model, expectedMessage, modelSorted);
    }

    @Test
    public void sortEmptyList_fail() throws Exception {
        SortPetCommand command = prepareCommand(modelWithNoClientPetAssociationList);
        assertCommandFailure(command, modelWithNoClientPetAssociationList, Messages.MESSAGE_CLIENTPETLIST_EMPTY);
    }

    @Test
    public void equal() throws Exception {
        SortPetCommand command = prepareCommand(model);
        command.execute();

        // Same objects -> return true
        assertEquals(command, command);

        // Different types -> return false
        assertFalse(command.equals(new ClearCommand()));

        // Different references -> return false
        SortPetCommand commandDiff = prepareCommand(model);
        commandDiff.execute();
        assertFalse(command.equals(commandDiff));

    }

    private SortPetCommand prepareCommand(Model model) {
        SortPetCommand command = new SortPetCommand();
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
