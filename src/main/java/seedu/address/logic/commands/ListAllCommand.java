package seedu.address.logic.commands;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.association.ClientOwnPet;
import seedu.address.model.client.Client;
import seedu.address.model.pet.Pet;

//@author purplepers0n
/**
 * Lists all details of a client, pet, technician or appointment in the address book.
 */
public class ListAllCommand extends Command {

    public static final String COMMAND_WORD = "listall";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all associated details of the specified index of the specified type\n"
            + "Parameters: INDEX\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Listed all details of client %1$s";

    private final Index targetIndex;
    private Client displayClient;
    private ObservableList<Pet> displayPet = FXCollections.observableArrayList();
    private ObservableList<Appointment> displayAppt = FXCollections.observableArrayList();

    public ListAllCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {

        if (targetIndex.getZeroBased() >= model.getFilteredClientList().size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        displayClient = model.getFilteredClientList().get(targetIndex.getZeroBased());
        setPets(displayClient);
        setAppts(displayPet);

        model.updateDetailsList(displayClient, displayPet, displayAppt);

        return new CommandResult(String.format(MESSAGE_SUCCESS, displayClient.getName().fullName));
    }

    private void setPets(Client client) {
        ObservableList<ClientOwnPet> clientOwnPets = model.getClientPetAssociationList();

        for (ClientOwnPet clientOwnPet : clientOwnPets) {
            if (clientOwnPet.getClient().equals(client)) {
                Pet currPet = clientOwnPet.getPet();
                displayPet.add(currPet);
            }
        }
    }

    private void setAppts(ObservableList<Pet> pets) {
        ObservableList<Appointment> appointmentList = model.getFilteredAppointmentList();

        for (Pet clientOwnPet : displayPet) {
            for (Appointment currAppt : appointmentList) {
                if (currAppt.getClientOwnPet() == null) {
                    continue;
                }
                if (currAppt.getClientOwnPet().getPet().equals(clientOwnPet)) {
                    displayAppt.add(currAppt);
                }
            }
        }
    }
}
