package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_AGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_GENDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.pet.Pet;
import seedu.address.model.pet.exceptions.DuplicatePetException;

/**
 * Adds support for adding a pet into the program.
 */
public class AddPetCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "addp";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a pet to the program. "
            + "Parameters: "
            + PREFIX_PET_NAME + "PET NAME "
            + PREFIX_PET_AGE + "PET AGE "
            + PREFIX_PET_GENDER + "PET GENDER "
            + PREFIX_TAG + "TAG...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_PET_NAME + "Garfield "
            + PREFIX_PET_AGE + "5 "
            + PREFIX_PET_GENDER + "M "
            + PREFIX_TAG + "cat "
            + PREFIX_TAG + "tabby ";

    public static final String MESSAGE_SUCCESS = "New pet added: %1$s";
    public static final String MESSAGE_DUPLICATE_PET = "This pet already exists in the address book";

    private final Pet toAdd;

    /**
     * Creates a AddPetCommand to add the specified {@code pet}
     */
    public AddPetCommand(Pet pet) {
        requireNonNull(pet);

        this.toAdd = pet;
    }
    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            model.addPet(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (DuplicatePetException e) {
            throw new CommandException(MESSAGE_DUPLICATE_PET);
        }
    }
    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof AddPetCommand
                && toAdd.equals(((AddPetCommand) other).toAdd));
    }

}
