package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showClientAtIndex;
import static seedu.address.logic.commands.CommandTestUtil.showPetAtIndex;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CLIENTS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PETS;
import static seedu.address.testutil.TypicalAddressBook.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PET;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PET;

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
import seedu.address.model.association.exceptions.ClientAlreadyOwnsPetException;
import seedu.address.model.association.exceptions.PetAlreadyHasOwnerException;
import seedu.address.model.client.Client;
import seedu.address.model.pet.Pet;

//@@author jonathanwj-reused
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand)
 * and unit tests for RemovePetFromClient.
 */
public class RemovePetFromClientCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Before
    public void setup() throws ClientAlreadyOwnsPetException, PetAlreadyHasOwnerException {
        model.addPetToClient(model.getAddressBook().getPetList().get(0),
                model.getAddressBook().getClientList().get(0));
        model.updateFilteredPetList(PREDICATE_SHOW_ALL_PETS);
        model.updateFilteredClientList(PREDICATE_SHOW_ALL_CLIENTS);
    }

    @Test
    public void execute_removePetToClient_success() throws Exception {

        Pet petInFilteredList = model.getFilteredPetList().get(INDEX_FIRST_PET.getZeroBased());
        Client clientInFilteredList = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());

        RemovePetFromClientCommand rpfcCommand = prepareCommand(INDEX_FIRST_PET, INDEX_FIRST_PERSON);

        String expectedMessage = String.format(RemovePetFromClientCommand.MESSAGE_ADD_PET_TO_CLIENT_SUCCESS,
                petInFilteredList, clientInFilteredList);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.removePetFromClient(model.getFilteredPetList().get(0), model.getFilteredClientList().get(0));

        assertCommandSuccess(rpfcCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_removePetFromClient_throwsCommandExeception() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(RemovePetFromClientCommand.MESSAGE_CLIENT_PET_NOT_ASSOCIATED);
        prepareCommand(INDEX_FIRST_PET, INDEX_SECOND_PERSON).execute();
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidIndex_failure() {
        showClientAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndexClient = INDEX_SECOND_PERSON;

        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndexClient.getZeroBased() < model.getAddressBook().getClientList().size());

        RemovePetFromClientCommand rpfcCommand = prepareCommand(INDEX_FIRST_PET, outOfBoundIndexClient);

        assertCommandFailure(rpfcCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        showPetAtIndex(model, INDEX_FIRST_PET);
        Index outOfBoundIndexPet = INDEX_SECOND_PET;

        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndexPet.getZeroBased() < model.getAddressBook().getClientList().size());

        rpfcCommand = prepareCommand(outOfBoundIndexPet, INDEX_FIRST_PERSON);

        assertCommandFailure(rpfcCommand, model, Messages.MESSAGE_INVALID_PET_DISPLAYED_INDEX);
    }

    @Test
    public void equals() throws Exception {
        final RemovePetFromClientCommand standardCommand = prepareCommand(INDEX_FIRST_PET, INDEX_FIRST_PERSON);

        // same values -> returns true
        RemovePetFromClientCommand commandWithSameValues = prepareCommand(INDEX_FIRST_PET, INDEX_FIRST_PERSON);
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
        assertFalse(standardCommand.equals(new RemovePetFromClientCommand(INDEX_SECOND_PET, INDEX_SECOND_PERSON)));

    }

    /**
     * Returns an {@code RemovePetFromClientCommand} with parameters pet {@code index} and client {@code index}
     */
    private RemovePetFromClientCommand prepareCommand(Index petIndex, Index clientIndex) {
        RemovePetFromClientCommand rpfcCommand = new RemovePetFromClientCommand(petIndex, clientIndex);
        rpfcCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return rpfcCommand;
    }
}
