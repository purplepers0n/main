package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_AGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_GENDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.logic.commands.AddPetCommand;
import seedu.address.model.pet.Pet;

//@@author md-azsa
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
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_PET_NAME + pet.getPetName().toString());
        sb.append(PREFIX_PET_AGE + pet.getPetAge().value);
        sb.append(PREFIX_PET_GENDER + pet.getPetGender().toString());
        pet.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }
}
