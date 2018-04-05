package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLIENT_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_AGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_GENDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PETS;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.association.exceptions.ClientAlreadyOwnsPetException;
import seedu.address.model.association.exceptions.PetAlreadyHasOwnerException;
import seedu.address.model.client.Client;
import seedu.address.model.pet.Pet;
import seedu.address.model.pet.exceptions.DuplicatePetException;

//@@author md-azsa
/**
 * Adds support for adding a pet into the program.
 */
public class AddPetCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "addp";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a pet to a specified client. "
            + "Parameters: "
            + PREFIX_CLIENT_INDEX + "CLIENT INDEX "
            + PREFIX_PET_NAME + "PET NAME "
            + PREFIX_PET_AGE + "PET AGE "
            + PREFIX_PET_GENDER + "PET GENDER "
            + PREFIX_TAG + "TAG...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_CLIENT_INDEX + "1 "
            + PREFIX_PET_NAME + "Garfield "
            + PREFIX_PET_AGE + "5 "
            + PREFIX_PET_GENDER + "M "
            + PREFIX_TAG + "cat "
            + PREFIX_TAG + "tabby ";

    public static final String MESSAGE_SUCCESS = "New pet added: %1$s";
    public static final String MESSAGE_DUPLICATE_PET = "This pet already exists in the address book";
    public static final String MESSAGE_CLIENT_HAS_PET = "Client already has pet";
    public static final String MESSAGE_PET_HAS_OWNER = "Pet already has an owner";

    private final Pet petToAdd;
    private final Index clientIndex;

    private Optional<Client> client;


    /**
     * Creates a AddPetCommand to add the specified {@code pet}
     */
    public AddPetCommand(Pet pet, Index clientIndex) {
        requireNonNull(pet);
        this.clientIndex = clientIndex;

        this.petToAdd = pet;
    }

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);

        try {
            model.addPet(petToAdd);
            model.addPetToClient(petToAdd, client.get());
            model.updateFilteredPetList(PREDICATE_SHOW_ALL_PETS);
        } catch (DuplicatePetException e) {
            throw new CommandException(MESSAGE_DUPLICATE_PET);
        } catch (ClientAlreadyOwnsPetException e) {
            throw new CommandException(MESSAGE_CLIENT_HAS_PET);
        } catch (PetAlreadyHasOwnerException e) {
            throw new CommandException(MESSAGE_PET_HAS_OWNER);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, petToAdd));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Client> lastShownListClient = model.getFilteredClientList();
        if (clientIndex.getZeroBased() >= lastShownListClient.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        client = Optional.of(lastShownListClient.get(clientIndex.getZeroBased()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof AddPetCommand
                && petToAdd.equals(((AddPetCommand) other).petToAdd));
    }

}
