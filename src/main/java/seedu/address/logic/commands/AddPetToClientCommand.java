package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLIENT_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_INDEX;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.association.exceptions.ClientAlreadyOwnsPetException;
import seedu.address.model.association.exceptions.PetAlreadyHasOwnerException;
import seedu.address.model.client.Client;
import seedu.address.model.pet.Pet;

/**
 * Edits the details of an existing person in the address book.
 */
public class AddPetToClientCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "addpettoclient";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Add a owner to a pet "
            + "by the index number used in the last client and pet listing.\n"
            + "Parameters: "
            + PREFIX_PET_INDEX + "PET_INDEX "
            + PREFIX_CLIENT_INDEX + "CLIENT_INDEX\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_PET_INDEX + "1 " + PREFIX_CLIENT_INDEX + "2";

    public static final String MESSAGE_ADD_PET_TO_CLIENT_SUCCESS = "Added Pet To client:\n%1$s\n>> %2$s";
    public static final String MESSAGE_CLIENT_HAS_PET = "Client already has pet";
    public static final String MESSAGE_PET_HAS_OWNER = "Pet already has an owner";

    private final Index petIndex;
    private final Index clientIndex;

    private Pet pet;
    private Client client;

    /**
     * @param petIndex of the pet in the filtered pet list to add
     * @param clientIndex of the person in the filtered client list to add pet to
     */
    public AddPetToClientCommand(Index petIndex, Index clientIndex) {
        requireNonNull(petIndex);
        requireNonNull(clientIndex);

        this.petIndex = petIndex;
        this.clientIndex = clientIndex;

        pet = null;
        client = null;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            model.addPetToClient(pet, client);
        } catch (ClientAlreadyOwnsPetException e) {
            throw new CommandException(MESSAGE_CLIENT_HAS_PET);
        } catch (PetAlreadyHasOwnerException e) {
            throw new CommandException(MESSAGE_PET_HAS_OWNER);
        }
        return new CommandResult(String.format(MESSAGE_ADD_PET_TO_CLIENT_SUCCESS, pet, client));

    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Pet> lastShownListPet = model.getFilteredPetList();
        List<Client> lastShownListClient = model.getFilteredClientList();

        if (petIndex.getZeroBased() >= lastShownListPet.size()
                || clientIndex.getZeroBased() >= lastShownListClient.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        pet = lastShownListPet.get(petIndex.getZeroBased());
        client = lastShownListClient.get(clientIndex.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddPetToClientCommand)) {
            return false;
        }

        // state check
        AddPetToClientCommand e = (AddPetToClientCommand) other;
        return petIndex.equals(e.petIndex)
                && clientIndex.equals(e.clientIndex)
                && pet.equals(e.pet)
                && client.equals(e.client);
    }

}
