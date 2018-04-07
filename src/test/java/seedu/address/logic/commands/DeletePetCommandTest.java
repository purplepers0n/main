package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalAppointments.APPOINTMENT_1;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPets.GARFIELD;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.association.exceptions.ClientPetAssociationNotFoundException;
import seedu.address.model.client.Client;
import seedu.address.model.pet.Pet;
import seedu.address.testutil.TypicalAddressBook;

//@@author md-azsa

/**
 * Contains integration tests unit tests for
 * {@code DeletePetCommand}.
 */
public class DeletePetCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(TypicalAddressBook.getTypicalAddressBook(), new UserPrefs());

    @Test
    public void removePet_invalidAssociation_throwClientPetAssociationNotFoundException() throws Exception {
        thrown.expect(ClientPetAssociationNotFoundException.class);
        model.deletePet(GARFIELD);
    }

    @Test
    public void removePet_petHasAppointment_success() throws Exception {
        model.addPetToClient(GARFIELD, (Client) FIONA);
        model.addAppointmentToPet(APPOINTMENT_1, GARFIELD);

        Pet petToDelete = model.getFilteredPetList().get(INDEX_FIRST.getZeroBased());
        DeletePetCommand deletePetCommand = new DeletePetCommand(INDEX_FIRST);

        String expectedMessage = String.format(DeletePetCommand.MESSAGE_DELETE_PET_SUCCESS, petToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePet(petToDelete);
        deletePetCommand.setData(model, new CommandHistory(), new UndoRedoStack());

        assertCommandSuccess(deletePetCommand, model, expectedMessage, expectedModel);
    }

}
