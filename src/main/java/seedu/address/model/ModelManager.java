package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;
import seedu.address.model.client.Client;
import seedu.address.model.client.exceptions.ClientNotFoundException;
import seedu.address.model.client.exceptions.DuplicateClientException;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.vettechnician.VetTechnician;
import seedu.address.model.vettechnician.exceptions.DuplicateVetTechnicianException;
import seedu.address.model.vettechnician.exceptions.VetTechnicianNotFoundException;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Client> filteredClients;
    private final FilteredList<VetTechnician> filteredVetTechnicians;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        filteredClients = new FilteredList<>(this.addressBook.getClientList());
        filteredVetTechnicians = new FilteredList<>(this.addressBook.getVetTechnicianList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyAddressBook newData) {
        addressBook.resetData(newData);
        indicateAddressBookChanged();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        raise(new AddressBookChangedEvent(addressBook));
    }

    //Person

    @Override
    public synchronized void deletePerson(Person target) throws PersonNotFoundException {
        addressBook.removePerson(target);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void addPerson(Person person) throws DuplicatePersonException {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void scheduleAppointment(Appointment appointment) throws DuplicateAppointmentException {
        addressBook.scheduleAppointment(appointment);
        indicateAddressBookChanged();
    }

    @Override
    public void updatePerson(Person target, Person editedPerson)
            throws DuplicatePersonException, PersonNotFoundException {
        requireAllNonNull(target, editedPerson);

        addressBook.updatePerson(target, editedPerson);
        indicateAddressBookChanged();
    }

    //Client

    @Override
    public synchronized void deleteClient(Client target) throws ClientNotFoundException {
        addressBook.removeClient(target);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void addClient(Client person) throws DuplicateClientException {
        addressBook.addClient(person);
        updateFilteredClientList(PREDICATE_SHOW_ALL_CLIENTS);
        indicateAddressBookChanged();
    }

    @Override
    public void updateClient(Client target, Client editedClient)
            throws DuplicateClientException, ClientNotFoundException {
        requireAllNonNull(target, editedClient);

        addressBook.updateClient(target, editedClient);
        indicateAddressBookChanged();
    }

    // VetTechnician

    @Override
    public synchronized void deleteVetTechnician(VetTechnician target) throws VetTechnicianNotFoundException {
        addressBook.removeVetTechnician(target);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void addVetTechnician(VetTechnician person) throws DuplicateVetTechnicianException {
        addressBook.addVetTechnician(person);
        updateFilteredVetTechnicianList(PREDICATE_SHOW_ALL_TECHNICIAN);
        indicateAddressBookChanged();
    }

    @Override
    public void updateVetTechnician(VetTechnician target, VetTechnician editedVetTechnician)
            throws DuplicateVetTechnicianException, VetTechnicianNotFoundException {
        requireAllNonNull(target, editedVetTechnician);

        addressBook.updateVetTechnician(target, editedVetTechnician);
        indicateAddressBookChanged();
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return FXCollections.unmodifiableObservableList(filteredPersons);
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    //Client

    /**
     * Returns an unmodifiable view of the list of {@code Client} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<Client> getFilteredClientList() {
        return FXCollections.unmodifiableObservableList(filteredClients);
    }

    @Override
    public void updateFilteredClientList(Predicate<Client> predicate) {
        requireNonNull(predicate);
        filteredClients.setPredicate(predicate);
    }

    //Vet Technician

    /**
     * Returns an unmodifiable view of the list of {@code VetTechnician} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<VetTechnician> getFilteredVetTechnicianList() {
        return FXCollections.unmodifiableObservableList(filteredVetTechnicians);
    }

    @Override
    public void updateFilteredVetTechnicianList(Predicate<VetTechnician> predicate) {
        requireNonNull(predicate);
        filteredVetTechnicians.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return addressBook.equals(other.addressBook)
                && filteredPersons.equals(other.filteredPersons)
                && filteredClients.equals(other.filteredClients)
                && filteredVetTechnicians.equals(other.filteredVetTechnicians);
    }

}
