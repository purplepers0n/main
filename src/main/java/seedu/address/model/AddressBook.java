package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.UniqueAppointmentList;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;
import seedu.address.model.client.Client;
import seedu.address.model.client.UniqueClientList;
import seedu.address.model.client.exceptions.ClientNotFoundException;
import seedu.address.model.client.exceptions.DuplicateClientException;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonRole;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.vettechnician.UniqueVetTechnicianList;
import seedu.address.model.vettechnician.VetTechnician;
import seedu.address.model.vettechnician.exceptions.DuplicateVetTechnicianException;
import seedu.address.model.vettechnician.exceptions.VetTechnicianNotFoundException;


/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .equals comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueTagList tags;
    private final UniqueAppointmentList appointments;

    private final UniqueClientList clients;
    private final UniqueVetTechnicianList technicians;


    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        tags = new UniqueTagList();

        clients = new UniqueClientList();
        technicians = new UniqueVetTechnicianList();

        appointments = new UniqueAppointmentList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons and Tags in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    public void setPersons(List<Person> persons) throws DuplicatePersonException {
        this.persons.setPersons(persons);
    }

    public void setClients(List<Client> clients) throws DuplicateClientException {
        this.clients.setClients(clients);
    }

    public void setVetTechnicians(List<VetTechnician> technicians) throws DuplicateVetTechnicianException {
        this.technicians.setVetTechnicians(technicians);
    }

    public void setTags(Set<Tag> tags) {
        this.tags.setTags(tags);
    }

    public void setAppointments(List<Appointment> appointments) throws DuplicateAppointmentException {
        this.appointments.setAppointments(appointments);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);
        setTags(new HashSet<>(newData.getTagList()));
        List<Person> syncedPersonList = newData.getPersonList().stream()
                .map(this::syncWithMasterTagList)
                .collect(Collectors.toList());
        List<Client> syncedClientList = newData.getClientList().stream()
                .map(client -> (Person) client)
                .map(this::syncWithMasterTagList)
                .map(person -> (Client) person)
                .collect(Collectors.toList());
        List<VetTechnician> syncedTechnicianList = newData.getVetTechnicianList().stream()
                .map(technician -> (Person) technician)
                .map(this::syncWithMasterTagList)
                .map(person -> (VetTechnician) person)
                .collect(Collectors.toList());
        List< Appointment > syncedAppointmentList = newData.getAppointmentList();
        try {
            setPersons(syncedPersonList);
            setClients(syncedClientList);
            setVetTechnicians(syncedTechnicianList);
        } catch (DuplicatePersonException | DuplicateClientException
                | DuplicateVetTechnicianException e) {
            throw new AssertionError("AddressBooks should not have duplicate persons");
        }

        try {
            setAppointments(syncedAppointmentList);
        } catch (DuplicateAppointmentException e) {
            throw new AssertionError("AddressBooks should not have duplicate appointments");
        }
    }

    //// person-level operations

    /**
     * Adds a person to the address book.
     * Also checks the new person's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the person to point to those in {@link #tags}.
     *
     * @throws DuplicatePersonException if an equivalent person already exists.
     */
    public void addPerson(Person p) throws DuplicatePersonException {
        Person person = syncWithMasterTagList(p);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any person
        // in the person list.
        persons.add(person);
    }

    /**
     * Adds a client to the address book.
     * Also checks the new client's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the client to point to those in {@link #tags}.
     *
     * @throws DuplicateClientException if an equivalent client already exists.
     */
    public void addClient(Client c) throws DuplicateClientException {
        Person person = syncWithMasterTagList(c);
        Client client = (Client) person;
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any person
        // in the person list.
        clients.add(client);
    }

    /**
     * Adds a vetTechnician to the address book.
     * Also checks the new vetTechnician's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the vetTechnician to point to those in {@link #tags}.
     *
     * @throws DuplicateVetTechnicianException if an equivalent vetTechnician already exists.
     */
    public void addVetTechnician(VetTechnician c) throws DuplicateVetTechnicianException {
        Person person = syncWithMasterTagList(c);
        VetTechnician vetTechnician = (VetTechnician) person;
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any person
        // in the person list.
        technicians.add(vetTechnician);
    }

    /**
     * Replaces the given client {@code target} in the list with {@code editedClient}.
     * {@code AddressBook}'s tag list will be updated with the tags of {@code editedClient}.
     *
     * @throws DuplicateClientException if updating the person's details causes the person to be equivalent to
     *      another existing person in the list.
     * @throws ClientNotFoundException if {@code target} could not be found in the list.
     *
     * @see #syncWithMasterTagList(Person)
     */
    public void updateClient(Client target, Client editedClient)
            throws DuplicateClientException, ClientNotFoundException {
        requireNonNull(editedClient);

        Person syncedEditedPerson = syncWithMasterTagList(editedClient);
        Client syncedEditedClient = (Client) syncedEditedPerson;
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any person
        // in the person list.
        clients.setClient(target, syncedEditedClient);
    }

    /**
     * Replaces the given vetTechnician {@code target} in the list with {@code editedVetTechnician}.
     * {@code AddressBook}'s tag list will be updated with the tags of {@code editedVetTechnician}.
     *
     * @throws DuplicateVetTechnicianException if updating the person's details causes the person to be equivalent to
     *      another existing person in the list.
     * @throws VetTechnicianNotFoundException if {@code target} could not be found in the list.
     *
     * @see #syncWithMasterTagList(Person)
     */
    public void updateVetTechnician(VetTechnician target, VetTechnician editedVetTechnician)
            throws DuplicateVetTechnicianException, VetTechnicianNotFoundException {
        requireNonNull(editedVetTechnician);

        Person syncedEditedPerson = syncWithMasterTagList(editedVetTechnician);
        VetTechnician syncedEditedVetTechnician = (VetTechnician) syncedEditedPerson;
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any person
        // in the person list.
        technicians.setVetTechnician(target, syncedEditedVetTechnician);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code AddressBook}'s tag list will be updated with the tags of {@code editedPerson}.
     *
     * @throws DuplicatePersonException if updating the client's details causes the client to be equivalent to
     *      another existing client in the list.
     * @throws PersonNotFoundException if {@code target} could not be found in the list.
     *
     * @see #syncWithMasterTagList(Person)
     */
    public void updatePerson(Person target, Person editedPerson)
            throws DuplicatePersonException, PersonNotFoundException {
        requireNonNull(editedPerson);

        Person syncedEditedPerson = syncWithMasterTagList(editedPerson);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any client
        // in the client list.
        persons.setPerson(target, syncedEditedPerson);
    }

    /**
     *  Updates the master tag list to include tags in {@code person} that are not in the list.
     *  @return a copy of this {@code person} such that every tag in this person points to a Tag object in the master
     *  list.
     */
    private Person syncWithMasterTagList(Person person) {
        Person syncedPerson;

        final UniqueTagList personTags = new UniqueTagList(person.getTags());
        tags.mergeFrom(personTags);

        // Create map with values = tag object references in the master list
        // used for checking person tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of person tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        personTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));

        if (PersonRole.isClient(person)) {
            syncedPerson = new Client(person.getName(), person.getPhone(), person.getEmail(),
                    person.getAddress(), correctTagReferences);
        } else {
            syncedPerson = new VetTechnician(person.getName(), person.getPhone(), person.getEmail(),
                    person.getAddress(), correctTagReferences);
        }
        return syncedPerson;
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     *
     * @throws PersonNotFoundException if the {@code key} is not in this {@code AddressBook}.
     */
    public boolean removePerson(Person key) throws PersonNotFoundException {
        if (persons.remove(key)) {
            return true;
        } else {
            throw new PersonNotFoundException();
        }
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     *
     * @throws ClientNotFoundException if the {@code key} is not in this {@code AddressBook}.
     */
    public boolean removeClient(Client key) throws ClientNotFoundException {
        if (clients.remove(key)) {
            return true;
        } else {
            throw new ClientNotFoundException();
        }
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     *
     * @throws VetTechnicianNotFoundException if the {@code key} is not in this {@code AddressBook}.
     */
    public boolean removeVetTechnician(VetTechnician key) throws VetTechnicianNotFoundException {
        if (technicians.remove(key)) {
            return true;
        } else {
            throw new VetTechnicianNotFoundException();
        }
    }

    //// tag-level operations

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }

    //// appointment-level operations

    /**
     * Schedule an appointment to the address book.
     * @throws DuplicateAppointmentException if an equivalent person already exists.
     */
    public void scheduleAppointment(Appointment a) throws DuplicateAppointmentException {
        appointments.add(a);
    }

    //// util methods

    @Override
    public String toString() {
        return persons.asObservableList().size() + " persons, " + tags.asObservableList().size() + " tags";
        // TODO: refine later
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asObservableList();
    }

    @Override
    public ObservableList<Client> getClientList() {
        return clients.asObservableList();
    }

    @Override
    public ObservableList<VetTechnician> getVetTechnicianList() {
        return technicians.asObservableList();
    }

    @Override
    public ObservableList<Tag> getTagList() {
        return tags.asObservableList();
    }

    @Override
    public ObservableList<Appointment> getAppointmentList() {
        return appointments.asObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && this.persons.equals(((AddressBook) other).persons)
                && this.tags.equalsOrderInsensitive(((AddressBook) other).tags))
                && this.appointments.equals(((AddressBook) other).appointments);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(persons, tags);
    }
}
