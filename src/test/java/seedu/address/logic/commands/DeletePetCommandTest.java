package seedu.address.logic.commands;

import static seedu.address.testutil.TypicalPets.GARFIELD;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.association.exceptions.ClientPetAssociationNotFoundException;
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

}
