package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.association.ClientOwnPet;
import seedu.address.model.client.Client;
import seedu.address.model.person.Person;
import seedu.address.model.pet.Pet;
import seedu.address.model.tag.Tag;
import seedu.address.model.vettechnician.VetTechnician;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns an unmodifiable view of the tags list.
     * This list will not contain any duplicate tags.
     */
    ObservableList<Tag> getTagList();

    /**
     * Returns an unmodifiable view of the appointments list.
     * This list will not contain any duplicate appointments.
     */
    ObservableList<Appointment> getAppointmentList();

    /**
     * Returns an unmodifiable view of the pets list.
     */
    ObservableList<Pet> getPetList();

    /**
     * Returns an unmodifiable view of the client pet association list.
     */
    ObservableList<ClientOwnPet> getClientPetAssociations();

    /**
     * Returns an unmodifiable view of the client list.
     */
    ObservableList<Client> getClientList();


    /**
     * Returns an unmodifiable view of the vet technician list.
     */
    ObservableList<VetTechnician> getVetTechnicianList();

}
