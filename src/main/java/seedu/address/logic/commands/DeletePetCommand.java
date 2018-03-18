package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.model.pet.Pet;
import seedu.address.model.pet.exceptions.PetNotFoundException;

/**
 * Deletes a pet identified using it's last displayed index from the program
 */
public class DeletePetCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "deletep";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the pet identified by the indexnumber used in the last pet listing\n"
            + "Parameters: INDEX (must be a postive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PET_SUCCESS = "Delete Pet: %1$s";

    private final Index targetIndex;

    private Pet petToDelete;

    public DeletePetCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(petToDelete);
        try {
            model.deletePet(petToDelete);
        } catch (PetNotFoundException pnfe) {
            throw new AssertionError("The target pet cannot be missing");
        }
        return new CommandResult(String.format(MESSAGE_DELETE_PET_SUCCESS, petToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof DeletePetCommand
                && this.targetIndex.equals(((DeletePetCommand) other).targetIndex)
                && Objects.equals(this.petToDelete, ((DeletePetCommand) other).petToDelete));
    }
}
