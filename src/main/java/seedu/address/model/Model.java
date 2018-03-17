package seedu.address.model;

import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;
import seedu.address.model.client.Client;
import seedu.address.model.client.exceptions.ClientNotFoundException;
import seedu.address.model.client.exceptions.DuplicateClientException;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.pet.Pet;
import seedu.address.model.pet.exceptions.DuplicatePetException;
import seedu.address.model.vettechnician.VetTechnician;
import seedu.address.model.vettechnician.exceptions.DuplicateVetTechnicianException;
import seedu.address.model.vettechnician.exceptions.VetTechnicianNotFoundException;


/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;
    Predicate<Client> PREDICATE_SHOW_ALL_CLIENTS = unused -> true;
    Predicate<VetTechnician> PREDICATE_SHOW_ALL_TECHNICIAN = unused -> true;

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyAddressBook newData);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /** Deletes the given person. */
    void deletePerson(Person target) throws PersonNotFoundException;

    /** Adds the given person */
    void addPerson(Person person) throws DuplicatePersonException;

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     *
     * @throws DuplicatePersonException if updating the person's details causes the person to be equivalent to
     *      another existing person in the list.
     * @throws PersonNotFoundException if {@code target} could not be found in the list.
     */
    void updatePerson(Person target, Person editedPerson)
            throws DuplicatePersonException, PersonNotFoundException;

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);


    //Client API

    /** Deletes the given client. */
    void deleteClient(Client target) throws ClientNotFoundException;

    /** Adds the given client */
    void addClient(Client client) throws DuplicateClientException;

    /**
     * Replaces the given client {@code target} with {@code editedClient}.
     *
     * @throws DuplicateClientException if updating the client's details causes the client to be equivalent to
     *      another existing client in the list.
     * @throws ClientNotFoundException if {@code target} could not be found in the list.
     */
    void updateClient(Client target, Client editedClient)
            throws DuplicateClientException, ClientNotFoundException;

    /** Returns an unmodifiable view of the filtered client list */
    ObservableList<Client> getFilteredClientList();

    /**
     * Updates the filter of the filtered client list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredClientList(Predicate<Client> predicate);

    //VetTechnician API

    /** Deletes the given vetTechnician. */
    void deleteVetTechnician(VetTechnician target) throws VetTechnicianNotFoundException;

    /** Adds the given vetTechnician */
    void addVetTechnician(VetTechnician vetTechnician) throws DuplicateVetTechnicianException;

    /**
     * Replaces the given vetTechnician {@code target} with {@code editedVetTechnician}.
     *
     * @throws DuplicateVetTechnicianException if updating
     * the vetTechnician's details causes the vetTechnician to be equivalent to
     * another existing vetTechnician in the list.
     * @throws VetTechnicianNotFoundException if {@code target} could not be found in the list.
     */
    void updateVetTechnician(VetTechnician target, VetTechnician editedVetTechnician)
            throws DuplicateVetTechnicianException, VetTechnicianNotFoundException;

    /** Returns an unmodifiable view of the filtered vetTechnician list */
    ObservableList<VetTechnician> getFilteredVetTechnicianList();

    /**
     * Updates the filter of the filtered vetTechnician list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredVetTechnicianList(Predicate<VetTechnician> predicate);

    /** Schedule the given appointment according to date and time */
    void scheduleAppointment(Appointment appointment) throws DuplicateAppointmentException;
    /** Adds the given pet */
    void addPet(Pet pet) throws DuplicatePetException;
}
