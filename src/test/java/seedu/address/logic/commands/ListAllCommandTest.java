package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import javafx.collections.FXCollections;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.association.ClientOwnPet;
import seedu.address.model.client.Client;
import seedu.address.model.pet.Pet;

//@@author purplepers0n

/**
 * Contains integration tests (interaction with the Model) for {@code ListAllCommand}.
 */
public class ListAllCommandTest {

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_invalidIndex_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundsIndex = INDEX_SECOND_PERSON;

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndex_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        assertExecutionSuccess(INDEX_FIRST_PERSON);
    }

    /**
     * Executes a {@code ListAllCommand} with the given {@code index}
     */
    private void assertExecutionSuccess(Index index) {
        ListAllCommand listAllCommand = prepareCommand(index);

        try {
            CommandResult commandResult = listAllCommand.execute();
            assertEquals(String.format(ListAllCommand.MESSAGE_SUCCESS,
                    model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased()).getName().fullName),
                    commandResult.feedbackToUser);
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }

        Client expectedClient = model.getFilteredClientList().get(index.getZeroBased());

        assertEquals(expectedClient, model.getClientDetails());

        List<ClientOwnPet> allClientOwnPets = model.getFilteredClientPetAssociationList();
        List<Pet> expectedPetList = FXCollections.observableArrayList();

        for (ClientOwnPet clientOwnPet : allClientOwnPets) {
            if (clientOwnPet.getClient().equals(expectedClient)) {
                Pet currPet = clientOwnPet.getPet();
                expectedPetList.add(currPet);
            }
        }
        assertEquals(expectedPetList, model.getClientPetList());

        List<Appointment> allApptList = model.getFilteredAppointmentList();
        List<Appointment> expectedApptList = FXCollections.observableArrayList();

        for (Appointment appt : allApptList) {
            for (Pet pet : expectedPetList) {
                if (appt.getClientOwnPet() != null && appt.getClientOwnPet().getPet().equals(pet)) {
                    expectedApptList.add(appt);
                }
            }
        }
        assertEquals(expectedApptList, model.getClientApptList());
    }

    /**
     * Executes a {@code ListAllCommand} with the given {@code index}, and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(Index index, String expectedMessage) {
        ListAllCommand listAllCommand = prepareCommand(index);

        try {
            listAllCommand.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(expectedMessage, ce.getMessage());
        }
    }

    /**
     * Returns a {@code ListAllCommand} with parameters {@code index}.
     */
    private ListAllCommand prepareCommand(Index index) {
        ListAllCommand listAllCommand = new ListAllCommand(index);
        listAllCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return listAllCommand;
    }
}
