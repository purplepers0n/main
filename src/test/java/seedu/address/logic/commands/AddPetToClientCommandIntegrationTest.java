package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalAddressBook.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PET;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.Before;
import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.client.Client;
import seedu.address.model.pet.Pet;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddPetToClientCommandIntegrationTest {

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_addPetToClient_success() throws Exception {
        Pet petInFilteredList = model.getFilteredPetList().get(INDEX_FIRST_PET.getZeroBased());
        Client clientInFilteredList = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPetToClient(petInFilteredList, clientInFilteredList);

        AddPetToClientCommand aptcCommand = prepareCommand(INDEX_FIRST_PET, INDEX_FIRST_PERSON);
        String expectedMessage = String.format(AddPetToClientCommand.MESSAGE_ADD_PET_TO_CLIENT_SUCCESS,
                petInFilteredList, clientInFilteredList);

        assertCommandSuccess(aptcCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_clientAlreadyOwnsPet_throwsCommandException() throws Exception {
        AddPetToClientCommand aptcCommand = prepareCommand(INDEX_FIRST_PET, INDEX_FIRST_PERSON);
        aptcCommand.execute();

        assertCommandFailure(aptcCommand, model, AddPetToClientCommand.MESSAGE_CLIENT_HAS_PET);
    }

    @Test
    public void execute_petAlreadyHasOwner_throwsCommandException() throws Exception {
        AddPetToClientCommand aptcCommand = prepareCommand(INDEX_FIRST_PET, INDEX_FIRST_PERSON);
        aptcCommand.execute();
        aptcCommand = prepareCommand(INDEX_FIRST_PET, INDEX_SECOND_PERSON);

        assertCommandFailure(aptcCommand, model, AddPetToClientCommand.MESSAGE_PET_HAS_OWNER);
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
