package seedu.address.logic.commands;

import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
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

    public static final String MESSAGE_SUCCESS = "Listed all details of client %2$s";

    private final Index targetIndex;
    private Client displayClient;
    private ObservableList<Pet> displayPet;
    private ObservableList<Appointment> displayAppt;

    private ObservableList<ClientOwnPet> currClientOwnPet;

    public ListAllCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() {

        displayClient = model.getFilteredClientList().get(targetIndex.getOneBased());
        setPets(displayClient);
        setAppts(displayPet);

        return new CommandResult(String.format(MESSAGE_SUCCESS, targetIndex));
    }

    private void setPets(Client client) {
        ObservableList<ClientOwnPet> clientOwnPets = model.getClientPetAssociationList();

        for (ClientOwnPet clientOwnPet : clientOwnPets) {
            if (clientOwnPet.getClient().equals(client)) {
                Pet currPet = clientOwnPet.getPet();
                displayPet.add(currPet);
                currClientOwnPet.add(new ClientOwnPet(client, currPet));
            }
        }
    }

    private void setAppts(ObservableList<Pet> pets) {
        ObservableList<Appointment> appointmentList = model.getFilteredAppointmentList();

        for (Appointment currAppt : appointmentList) {
            for (ClientOwnPet clientOwnPet : currClientOwnPet) {
                if (currAppt.getClientOwnPet().equals(clientOwnPet)) {
                    displayAppt.add(currAppt);
                }
            }
        }
    }
}
