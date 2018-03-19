package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPets.getTypicalAddressBookPet;

import org.junit.Before;
import org.junit.Test;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.pet.Pet;
import seedu.address.testutil.PetBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddPetCommand}
 */
public class AddPetCommandIntegrationTest {

    private Model model;

    @Before
    public void setup() {
        model = new ModelManager(getTypicalAddressBookPet(),
                new UserPrefs());
    }

    @Test
    public void execute_newPet_success() throws Exception {
        Pet validPet = new PetBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(),
                new UserPrefs());
        expectedModel.addPet(validPet);

        assertCommandSuccess(prepareCommand(validPet, model), model,
                String.format(AddPetCommand.MESSAGE_SUCCESS, validPet), expectedModel);
    }

    @Test
    public void execute_duplicatePet_throwsCommandException() {
        Pet petInList = model.getAddressBook().getPetList().get(0);
        assertCommandFailure(prepareCommand(petInList, model), model, AddPetCommand.MESSAGE_DUPLICATE_PET);
    }

    /**
     * Generates a new {@code AddPetCommand} which upon execution,
     * adds {@code Pet} into {@code model}
     */
    private AddPetCommand prepareCommand(Pet pet, Model model) {
        AddPetCommand command = new AddPetCommand(pet);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
