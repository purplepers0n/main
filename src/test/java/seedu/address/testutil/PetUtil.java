package seedu.address.testutil;

import seedu.address.logic.commands.AddPetCommand;
import seedu.address.model.pet.Pet;

/**
 * Util class for pet.
 */
public class PetUtil {

    /**
     * Returns add command string for adding the {@code pet}
     */
    public static String getAddPetCommand(Pet pet) {
        return AddPetCommand.COMMAND_WORD + " " + getPetDetails(pet);
    }

    /**
     * Returns the details of the pet
     */
    public static String getPetDetails(Pet pet) {
        
    }
}
