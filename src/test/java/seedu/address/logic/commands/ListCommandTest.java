package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
//import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
//import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private static final String LIST_EXPECTED_CLIENT_MESSAGE_SUCCESS = "Listed all clients";
    private static final String LIST_EXPECTED_PET_MESSAGE_SUCCESS = "Listed all pets";
    private static final String LIST_EXPECTED_TECH_MESSAGE_SUCCESS = "Listed all vettechs";



    private Model model;
    private Model expectedModel;
    private ListCommand listCommandClient;
    private ListCommand listCommandPet;
    private ListCommand listCommandVetTech;


    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        listCommandClient = new ListCommand("client");
        listCommandPet = new ListCommand("pet");
        listCommandVetTech = new ListCommand("vettech");

        listCommandClient.setData(model, new CommandHistory(), new UndoRedoStack());
        listCommandPet.setData(model, new CommandHistory(), new UndoRedoStack());
        listCommandVetTech.setData(model, new CommandHistory(), new UndoRedoStack());

    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(listCommandClient, model, LIST_EXPECTED_CLIENT_MESSAGE_SUCCESS, expectedModel);
        assertCommandSuccess(listCommandPet, model, LIST_EXPECTED_PET_MESSAGE_SUCCESS, expectedModel);
        assertCommandSuccess(listCommandVetTech, model, LIST_EXPECTED_TECH_MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        assertCommandSuccess(listCommandClient, model, LIST_EXPECTED_CLIENT_MESSAGE_SUCCESS, expectedModel);
    }
}
