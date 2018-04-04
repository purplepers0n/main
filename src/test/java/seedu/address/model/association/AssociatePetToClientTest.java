package seedu.address.model.association;

import static junit.framework.TestCase.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.showClientAtIndex;
import static seedu.address.logic.commands.CommandTestUtil.showPetAtIndex;
import static seedu.address.testutil.TypicalAddressBook.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PET;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.index.Index;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.association.exceptions.ClientAlreadyOwnsPetException;
import seedu.address.model.association.exceptions.ClientPetAssociationNotFoundException;
import seedu.address.model.association.exceptions.PetAlreadyHasOwnerException;
import seedu.address.model.client.Client;
import seedu.address.model.pet.Pet;

//@@author jonathanwj
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand)
 * and unit tests for associating pet to client.
 */
public class AssociatePetToClientTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void removePetFromClient_alreadyAssociated_success() throws ClientAlreadyOwnsPetException,
            PetAlreadyHasOwnerException,
            ClientPetAssociationNotFoundException {
        showPetAtIndex(model, Index.fromOneBased(4));
        showClientAtIndex(model, Index.fromOneBased(6));

        Pet petInFilteredList = model.getFilteredPetList().get(INDEX_FIRST_PET.getZeroBased());
        Client clientInFilteredList = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());

        AddressBook expectedAddressBook = getTypicalAddressBook();
        expectedAddressBook.removePetFromClient(petInFilteredList, clientInFilteredList);

        model.removePetFromClient(petInFilteredList, clientInFilteredList);

        Model expectedModel = new ModelManager(new AddressBook(expectedAddressBook), new UserPrefs());

        model.updateFilteredPetList(Model.PREDICATE_SHOW_ALL_PET);
        model.updateFilteredClientList(Model.PREDICATE_SHOW_ALL_CLIENTS);

        assertEquals(expectedModel, model);
    }

    @Test
    public void removePetFromClient_noAssociated_throwsClientPetAssociationNotFoundException()
            throws ClientAlreadyOwnsPetException, PetAlreadyHasOwnerException, ClientPetAssociationNotFoundException {
        thrown.expect(ClientPetAssociationNotFoundException.class);
        showPetAtIndex(model, Index.fromOneBased(3));
        showClientAtIndex(model, Index.fromOneBased(6));

        Pet petInFilteredList = model.getFilteredPetList().get(INDEX_FIRST_PET.getZeroBased());
        Client clientInFilteredList = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());

        model.removePetFromClient(petInFilteredList, clientInFilteredList);

    }

    @Test
    public void addPettoClient_noAssociation_success() throws ClientAlreadyOwnsPetException,
            PetAlreadyHasOwnerException,
            ClientPetAssociationNotFoundException {
        showPetAtIndex(model, Index.fromOneBased(2));
        showClientAtIndex(model, Index.fromOneBased(6));

        Pet petInFilteredList = model.getFilteredPetList().get(INDEX_FIRST_PET.getZeroBased());
        Client clientInFilteredList = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());

        AddressBook expectedAddressBook = getTypicalAddressBook();
        expectedAddressBook.addPetToClient(petInFilteredList, clientInFilteredList);

        model.addPetToClient(petInFilteredList, clientInFilteredList);

        Model expectedModel = new ModelManager(new AddressBook(expectedAddressBook), new UserPrefs());

        model.updateFilteredPetList(Model.PREDICATE_SHOW_ALL_PET);
        model.updateFilteredClientList(Model.PREDICATE_SHOW_ALL_CLIENTS);

        assertEquals(expectedModel, model);
    }

    @Test
    public void addPettoClient_alreadyAssociated_throwsClientAlreadyOwnsPetException()
            throws ClientAlreadyOwnsPetException, PetAlreadyHasOwnerException, ClientPetAssociationNotFoundException {
        thrown.expect(ClientAlreadyOwnsPetException.class);
        showPetAtIndex(model, Index.fromOneBased(4));
        showClientAtIndex(model, Index.fromOneBased(6));

        Pet petInFilteredList = model.getFilteredPetList().get(INDEX_FIRST_PET.getZeroBased());
        Client clientInFilteredList = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());

        model.addPetToClient(petInFilteredList, clientInFilteredList);

    }

    @Test
    public void addPettoClient_petAlreadyHasOwner_throwsPetAlreadyHasOwnerException()
            throws ClientAlreadyOwnsPetException, PetAlreadyHasOwnerException, ClientPetAssociationNotFoundException {
        thrown.expect(PetAlreadyHasOwnerException.class);
        showPetAtIndex(model, Index.fromOneBased(4));
        showClientAtIndex(model, Index.fromOneBased(4));

        Pet petInFilteredList = model.getFilteredPetList().get(INDEX_FIRST_PET.getZeroBased());
        Client clientInFilteredList = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());

        model.addPetToClient(petInFilteredList, clientInFilteredList);

    }

}
