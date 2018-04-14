package seedu.address.model;

import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.exceptions.AppointmentAlreadyHasVetTechnicianException;
import seedu.address.model.appointment.exceptions.AppointmentDoesNotHavePetException;
import seedu.address.model.appointment.exceptions.AppointmentHasBeenTakenException;
import seedu.address.model.appointment.exceptions.AppointmentListIsEmptyException;
import seedu.address.model.appointment.exceptions.AppointmentNotFoundException;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;
import seedu.address.model.association.ClientOwnPet;
import seedu.address.model.association.exceptions.ClientAlreadyOwnsPetException;
import seedu.address.model.association.exceptions.ClientPetAssociationListEmptyException;
import seedu.address.model.association.exceptions.ClientPetAssociationNotFoundException;
import seedu.address.model.association.exceptions.PetAlreadyHasAppointmentException;
import seedu.address.model.association.exceptions.PetAlreadyHasOwnerException;
import seedu.address.model.client.Client;
import seedu.address.model.client.exceptions.ClientHasExistingAppointmentException;
import seedu.address.model.client.exceptions.ClientHasExistingPetException;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.person.exceptions.PersonsListIsEmptyException;
import seedu.address.model.pet.Pet;
import seedu.address.model.pet.exceptions.DuplicatePetException;
import seedu.address.model.pet.exceptions.PetNotFoundException;
import seedu.address.model.vettechnician.VetTechnician;
import seedu.address.model.vettechnician.exceptions.TechnicianHasExistingAppointmentException;
import seedu.address.model.vettechnician.exceptions.VetTechnicianNotFoundException;


/**
 * The API of the Model component.
 */
public interface Model {
    /**
     * {@code Predicate} that always evaluate to true
     */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;
    Predicate<Client> PREDICATE_SHOW_ALL_CLIENTS = unused -> true;
    Predicate<Pet> PREDICATE_SHOW_ALL_PETS = unused -> true;
    Predicate<VetTechnician> PREDICATE_SHOW_ALL_TECHNICIAN = unused -> true;
    Predicate<Appointment> PREDICATE_SHOW_ALL_APPOINTMENT = unused -> true;

    /**
     * {@code Predicate} that always evaluate to true
     */
    Predicate<Pet> PREDICATE_SHOW_ALL_PET = unused -> true;
    Predicate<ClientOwnPet> PREDICATE_SHOW_ALL_ASSOCIATION = unused -> true;

    /**
     * Clears existing backing model and replaces with the provided new data.
     */
    void resetData(ReadOnlyAddressBook newData);

    /**
     * Returns the AddressBook
     */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Deletes the given person.
     */
    void deletePerson(Person target) throws PersonNotFoundException;

    /**
     * Adds the given person
     */
    void addPerson(Person person) throws DuplicatePersonException;

    /**
     * Sorts the given client list.
     */
    void sortClientList() throws PersonsListIsEmptyException;

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     *
     * @throws DuplicatePersonException if updating the person's details causes the person to be equivalent to
     *                                  another existing person in the list.
     * @throws PersonNotFoundException  if {@code target} could not be found in the list.
     */
    void updatePerson(Person target, Person editedPerson)
            throws DuplicatePersonException, PersonNotFoundException, ClientHasExistingAppointmentException,
            ClientHasExistingPetException, TechnicianHasExistingAppointmentException;

    /**
     * Returns an unmodifiable view of the filtered person list
     */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Returns an unmodifiable view of the filtered client list
     */
    ObservableList<Client> getFilteredClientList();

    /**
     * Updates the filter of the filtered client list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredClientList(Predicate<Client> predicate);

    /**
     * Returns an unmodifiable view of the filtered vetTechnician list
     */
    ObservableList<VetTechnician> getFilteredVetTechnicianList();

    /**
     * Updates the filter of the filtered vet technician list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredVetTechnicianList(Predicate<VetTechnician> predicate);

    /**
     * Schedule the given appointment according to date and time
     */
    void scheduleAppointment(Appointment appointment) throws DuplicateAppointmentException;

    /**
     * Unschedule the given appointment according to index
     */
    void unscheduleAppointment(Appointment appointment) throws
            AppointmentListIsEmptyException, AppointmentNotFoundException;

    /**
     * Reschedules the given appointment {@code target} with {@code rescheduledPerson}.
     *
     * @throws DuplicateAppointmentException if updating the appointment's details causes the appointment
     *                                       to be clashed with another existing appointment in the list.
     * @throws AppointmentNotFoundException  if {@code target} could not be found in the list.
     */
    void updateAppointment(Appointment target, Appointment rescheduledAppointment)
            throws DuplicateAppointmentException, AppointmentNotFoundException;

    /**
     * Returns an unmodifiable view of the filtered appointment list
     **/
    ObservableList<Appointment> getFilteredAppointmentList();

    /**
     * Adds the given pet
     */
    void addPet(Pet pet) throws DuplicatePetException;

    void deletePet(Pet pet) throws PetNotFoundException, ClientPetAssociationNotFoundException;

    /**
     * Returns an unmodifiable view of the filtered pet list
     */
    ObservableList<Pet> getFilteredPetList();

    /**
     * Updates the filter of the filtered pet list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPetList(Predicate<Pet> predicate);

    /**
     * Updates the filter of the filtered client pet association list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredClientOwnPetAssocation(Predicate<ClientOwnPet> predicate);

    /**
     * Sorts the pet list.
     */
    void sortPetList() throws ClientPetAssociationListEmptyException;

    /** Returns an unmodifiable view of the filtered client pet association list */
    ObservableList<ClientOwnPet> getFilteredClientPetAssociationList();

    /**
     * Associates pet to client
     */
    void addPetToClient(Pet pet, Client client) throws ClientAlreadyOwnsPetException, PetAlreadyHasOwnerException;

    /**
     * Removes association from pet and client
     */
    void removePetFromClient(Pet pet, Client client) throws ClientPetAssociationNotFoundException;

    /**
     * Updates the filteredAppointmentList
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredAppointmentList(Predicate<Appointment> predicate);

    /**
     * Adds an appointment to a pet.
     */
    void addAppointmentToPet(Appointment appointment, Pet pet) throws
            PetAlreadyHasAppointmentException, ClientPetAssociationNotFoundException,
            AppointmentNotFoundException, DuplicateAppointmentException, AppointmentHasBeenTakenException;

    /**
     * Removes an appointment from a pet.
     */
    void removeAppointmentFromPet(Appointment appointment) throws
            AppointmentNotFoundException, DuplicateAppointmentException, AppointmentDoesNotHavePetException;

    /**
     * Sorts the appointment by date and then by time
     */
    void sortAppointmentList() throws AppointmentListIsEmptyException;

    /**
     * Sets the index of the current list that is viewed
     */
    void setCurrentList(int currList);

    /**
     * Get the index of the current list that is viewed
     */
    int getCurrentList();

    /**
     * Adds the given Vet Technician to the given appointment
     */
    void addVetTechToAppointment(VetTechnician technician, Appointment appointment)
            throws AppointmentAlreadyHasVetTechnicianException,
            DuplicateAppointmentException, AppointmentNotFoundException;

    /**
     * Removes the vet from the given appointment
     */
    void removeVetTechFromAppointent(Appointment apptToRemoveVetFrom)
            throws DuplicateAppointmentException, AppointmentNotFoundException, VetTechnicianNotFoundException;

    /**
     * Updates the listed details of a client
     */
    void updateDetailsList(Client client, ObservableList<Pet> pets,
                           ObservableList<Appointment> appointments);

    /**
     * Returns the queried client
     **/
    Client getClientDetails();

    /**
     * Returns an unmodifiable view of the pet list of the queried client
     **/
    ObservableList<Pet> getClientPetList();

    /**
     * Returns an unmodifiable view of the appointment list of the queried client
     **/
    ObservableList<Appointment> getClientApptList();
}
