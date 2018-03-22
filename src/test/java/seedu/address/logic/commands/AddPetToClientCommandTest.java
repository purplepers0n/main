package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.prepareRedoCommand;
import static seedu.address.logic.commands.CommandTestUtil.prepareUndoCommand;
import static seedu.address.logic.commands.CommandTestUtil.showClientAtIndex;
import static seedu.address.logic.commands.CommandTestUtil.showPetAtIndex;
import static seedu.address.testutil.TypicalAddressBook.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PET;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PET;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.client.Client;
import seedu.address.model.pet.Pet;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand)
 * and unit tests for AddPetToClientCommand.
 */
public class AddPetToClientCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_petFilteredList_success() throws Exception {
        showPetAtIndex(model, INDEX_FIRST_PET);
        showClientAtIndex(model, INDEX_FIRST_PERSON);

        Pet petInFilteredList = model.getFilteredPetList().get(INDEX_FIRST_PET.getZeroBased());
        Client clientInFilteredList = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());

        AddPetToClientCommand aptcCommand = prepareCommand(INDEX_FIRST_PET, INDEX_FIRST_PERSON);

        String expectedMessage = String.format(AddPetToClientCommand.MESSAGE_ADD_PET_TO_CLIENT_SUCCESS,
                petInFilteredList, clientInFilteredList);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.addPetToClient(model.getFilteredPetList().get(0), model.getFilteredClientList().get(0));

        assertCommandSuccess(aptcCommand, model, expectedMessage, expectedModel);
    }


    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndexPet = Index.fromOneBased(model.getFilteredPetList().size() + 1);
        Index outOfBoundIndexClient = Index.fromOneBased(model.getFilteredClientList().size() + 1);

        AddPetToClientCommand aptcCommand = prepareCommand(outOfBoundIndexClient, outOfBoundIndexPet);

        assertCommandFailure(aptcCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPetAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndexPet = INDEX_SECOND_PERSON;
        Index outOfBoundIndexClient = INDEX_SECOND_PERSON;

        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndexPet.getZeroBased() < model.getAddressBook().getPetList().size());
        assertTrue(outOfBoundIndexClient.getZeroBased() < model.getAddressBook().getClientList().size());

        AddPetToClientCommand aptcCommand = prepareCommand(outOfBoundIndexPet, outOfBoundIndexClient);

        assertCommandFailure(aptcCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);

        AddPetToClientCommand aptcCommand = prepareCommand(INDEX_FIRST_PET, INDEX_FIRST_PERSON);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        // add -> pet added to client
        aptcCommand.execute();
        undoRedoStack.push(aptcCommand);

        // undo -> reverts addressbook back to previous state and filtered lists to show all
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> add pet back to client
        expectedModel.addPetToClient(model.getFilteredPetList().get(INDEX_FIRST_PET.getZeroBased()),
                model.getFilteredClientList().get(INDEX_FIRST_PET.getZeroBased()));
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndexPet = Index.fromOneBased(model.getFilteredPetList().size() + 1);
        Index outOfBoundIndexClient = Index.fromOneBased(model.getFilteredClientList().size() + 1);

        AddPetToClientCommand aptcCommand = prepareCommand(outOfBoundIndexPet, outOfBoundIndexClient);

        // execution failed -> editCommand not pushed into undoRedoStack
        assertCommandFailure(aptcCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Adss a {@code Pet} To a {@code Client} from filtered lists.
     * 2. Undo the command.
     * 3. The unfiltered lists should be shown now. Verify that the index of the previously edited person in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the command. This ensures {@code RedoCommand} adds the pet to client object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_samePersonEdited() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);

        AddPetToClientCommand aptcCommand = prepareCommand(INDEX_FIRST_PET, INDEX_FIRST_PERSON);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        showPetAtIndex(model, INDEX_SECOND_PET);
        showClientAtIndex(model, INDEX_SECOND_PERSON);
        Pet petToAdd = model.getFilteredPetList().get(INDEX_FIRST_PET.getZeroBased());
        Client clientToAddPetTo = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());
        // add -> add first client in filtered client list and the first pet in filtered pet list
        aptcCommand.execute();
        undoRedoStack.push(aptcCommand);

        // undo -> reverts addressbook back to previous state and filtered person list to show all clients and pets
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        assertNotEquals(model.getFilteredPetList().get(INDEX_FIRST_PET.getZeroBased()), petToAdd);
        assertNotEquals(model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased()), clientToAddPetTo);

        // redo -> add the same pet to client in unfiltered lists
        expectedModel.addPetToClient(petToAdd, clientToAddPetTo);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() throws Exception {
        final AddPetToClientCommand standardCommand = prepareCommand(INDEX_FIRST_PET, INDEX_FIRST_PERSON);

        // same values -> returns true
        AddPetToClientCommand commandWithSameValues = prepareCommand(INDEX_FIRST_PET, INDEX_FIRST_PERSON);
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
        assertFalse(standardCommand.equals(new AddPetToClientCommand(INDEX_SECOND_PET, INDEX_SECOND_PERSON)));

    }

    /**
     * Returns an {@code AddPetToClientCommand} with parameters pet {@code index} and client {@code index}
     */
    private AddPetToClientCommand prepareCommand(Index petIndex, Index clientIndex) {
        AddPetToClientCommand aptcCommand = new AddPetToClientCommand(petIndex, clientIndex);
        aptcCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return aptcCommand;
    }
}
